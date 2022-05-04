package it.redhat.mrt.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.FilenameFilter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.awt.Color;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.redhat.mrt.model.Report;
import it.redhat.mrt.model.Trip;

public class Builder {
    public static final String BANNER_RESOURCE = "/RHBannerNew.png";

    @ConfigProperty(name = "mrt.reports.dirname") 
    String dirname;

	private static final Logger logger = LoggerFactory.getLogger("it.redhat.mrt");

    private PDDocument document;
    private PDPageContentStream contentStream;
    private PDImageXObject bannerImage;

    private float borderWidth = 40;
    private float contentWidth;
    private float xLeft, xRight, xMiddle;
    private float imageY, topY;
    private Color darkBlue= new Color(0, 65, 85);
    private Color darkRed= new Color(175, 0, 0);
    private int monthlyTotalMileage;

    private Report report;

    public Builder setReport(Report report){
        this.report = report;
        return this;
    }
    public Builder setBaseDir(String basedir){
        this.dirname = basedir;
        return this;
    }

    public void build(){
        if (report == null) {
			throw new NullPointerException("Report not set for this builder.");
        }
        if((report.trips == null) || (report.trips.size() == 0)){
            // may be trips haven't been loaded
            // let's try now to load them
            report.trips = Trip.getTrips(report.userid, report.year, report.month);
        }
        logger.info("[PdfBuilder] building pdf report for user: " + this.report.name);
        try {
            this.init();
            this.drawBanner();
            this.drawHeaderLines();
            this.drawHeaderLabels();
            this.drawHeaderValues();
            this.drawFooterLines();
            this.drawFooterLabels();
            this.prepareTable();
            this.writeTripLogs();
            this.drawFooterValues();
            this.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private void init(){
        document = new PDDocument();
        document.addPage(new PDPage());
        PDPage page = document.getPage(0);
        ByteArrayOutputStream byteArrayOutputStream = null;
        InputStream inputStream = null;

        try {

        	// Read banner resource from jar into a byte array
        	byteArrayOutputStream = new ByteArrayOutputStream();
            inputStream = getClass().getResourceAsStream(BANNER_RESOURCE);
            byte[] buffer = new byte[4096];
            while (true) {
                int nread = inputStream.read(buffer);
                if (nread <= 0) {
                    break;
                }
                byteArrayOutputStream.write(buffer, 0, nread);
            }

            byte[] data = byteArrayOutputStream.toByteArray();

            bannerImage = PDImageXObject.createFromByteArray(document, data, null);
            contentStream = new PDPageContentStream(document, page);

        } catch (Throwable t) {
            StringWriter trace = new StringWriter();
            t.printStackTrace(new PrintWriter(trace, true));
            logger.warn(trace.toString());
        } finally {

        	// close opened resources
        	if (inputStream != null) try { inputStream.close(); } catch (Exception e) {logger.warn(e.getMessage());}

        	if (byteArrayOutputStream != null) try { byteArrayOutputStream.close(); } catch (Exception e) {logger.warn(e.getMessage());}

        }
        contentWidth = page.getBleedBox().getWidth() - (borderWidth * 2);
        xLeft = borderWidth;
        xRight = xLeft + contentWidth;
        xMiddle = xLeft + (contentWidth / 2);
        float imageHeight = (contentWidth / bannerImage.getWidth()) * bannerImage.getHeight();
        topY = page.getBleedBox().getHeight() - borderWidth;
        imageY = topY - imageHeight;
    }

    private void save(){
        // reports are saved in a directory based on /reports as root
        // then each associate has his own dir based on RHID
        // then each year has its own dir.
        String dirName = getReportDirectoryName();

        try {
            Files.createDirectories(Paths.get(dirName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filename = getReportFilename();
        try {
            if (contentStream != null){
                contentStream.close();
                contentStream = null;
            }
            document.save(dirName + "/" + filename);
        } catch (Throwable t) {
            StringWriter trace = new StringWriter();
            t.printStackTrace(new PrintWriter(trace, true));
            logger.warn(trace.toString());
        }
        logger.info("[Report Builder] report saved: " + dirName + "/" +filename);
    }

    private String getReportFilename(){
        File dir = new File(getReportDirectoryName());
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".pdf");
            }
        });

        int version = 1;
        String rootName = this.report.userid + "_" + this.report.year + "_" + this.report.month;
        for (int i = 0; i < files.length; i++) {
            String docName = files[i].getName();
            if(docName.startsWith(rootName)){
                String[] parts = docName.substring(0, docName.length() - 4).split("_");
                int ver = Integer.parseInt(parts[3]);
                if(ver >= version){
                    version = ver + 1;
                }
            }
        }
        return this.report.userid + "_" + this.report.year + "_" + this.report.month + "_" + version + ".pdf";
    }

    private String getReportDirectoryName(){
        return dirname + "/" + this.report.userid + "/" + this.report.year;
    }

    private void writeCellLabel(PDPageContentStream contentStream, int col, int row, String header) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, 6);
        contentStream.setNonStrokingColor(darkBlue);

        float cellWidth = contentWidth / 3;
        float text_width = (PDType1Font.HELVETICA.getStringWidth(header) / 1000.0f) * 6;
        float x = xLeft + (cellWidth * col) + (cellWidth / 2) - (text_width / 2);
        float y = imageY - (40 * row) - 18;

        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(header);
        contentStream.endText();
    }

    private void writeCellValue(int col, int row, String value) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.setNonStrokingColor(darkBlue);

        float text_width = (PDType1Font.HELVETICA_BOLD.getStringWidth(value) / 1000.0f) * 10;
        float cellWidth = contentWidth / 3;
        float x = xLeft + (cellWidth * col) + (cellWidth / 2) - (text_width / 2);
        float y = imageY - (40 * row) - 30;

        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(value);
        contentStream.endText();
    }

    private void drawBanner() throws IOException, URISyntaxException {
        float imageHeight = topY -imageY;
        contentStream.drawImage(bannerImage, xLeft, imageY, contentWidth, imageHeight);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
        contentStream.setNonStrokingColor(Color.white);
        contentStream.newLineAtOffset(65, imageY + 36);
        contentStream.showText("Business Mileage Reimbursement Form");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.COURIER, 10);
        contentStream.setNonStrokingColor(Color.white);
        contentStream.newLineAtOffset(65, imageY + 16);
        contentStream.showText("Produced by MRTool");
        contentStream.endText();
    }

    private void drawHeaderLines() throws IOException {
        contentStream.setNonStrokingColor(darkBlue);
        contentStream.setLineWidth(3);

        contentStream.moveTo(xLeft, imageY - 1);
        contentStream.lineTo(xRight, imageY - 1);
        contentStream.stroke();
        contentStream.moveTo(xLeft, imageY - 43);
        contentStream.lineTo(xRight, imageY - 43);
        contentStream.stroke();


        contentStream.setLineWidth(.5f);
        contentStream.setLineDashPattern(new float[]{3,1}, 0);

        /*
        contentStream.moveTo(50, imageY - 42);
        contentStream.lineTo(xRight - 10, imageY - 42);
        contentStream.stroke();
        contentStream.moveTo(50, imageY - 83);
        contentStream.lineTo(xRight - 10, imageY - 83);
        contentStream.stroke();
        */


        float cellWidth = (contentWidth - 20) / 3;
        contentStream.moveTo(50 + cellWidth, imageY - 35);
        contentStream.lineTo(50 + cellWidth, imageY - 10);
        contentStream.stroke();
        contentStream.moveTo(50 + (cellWidth*2), imageY - 35);
        contentStream.lineTo(50 + (cellWidth*2), imageY - 10);
        contentStream.stroke();
    }

    private void drawHeaderLabels() throws IOException {
        writeCellLabel(contentStream, 0, 0, "associate name");
        //writeCellLabel(contentStream, 0, 1, "cost center");
        //writeCellLabel(contentStream, 0, 2, "employee number");
        writeCellLabel(contentStream, 1, 0, "vehicle type");
        //writeCellLabel(contentStream, 1, 1, "car registry number");
        //writeCellLabel(contentStream, 1, 2, "mileage cost rate (euro/km)");
        writeCellLabel(contentStream, 2, 0, "mileage rate");
        //writeCellLabel(contentStream, 2, 1, "total from previous report (Km)");
        //writeCellLabel(contentStream, 2, 2, "this year total including this month (Km)");
    }

    private void drawFooterLines() throws IOException {
        contentStream.setNonStrokingColor(darkBlue);
        contentStream.setLineDashPattern(new float[]{}, 0);
        contentStream.setLineWidth(1);

        contentStream.moveTo(xLeft, 60);
        contentStream.lineTo(xRight, 60);
        contentStream.stroke();
        contentStream.moveTo(xLeft, 100);
        contentStream.lineTo(xRight, 100);
        contentStream.stroke();

        contentStream.setLineWidth(.5f);
        contentStream.setLineDashPattern(new float[]{3,1}, 0);

        contentStream.moveTo(xMiddle, 80);
        contentStream.lineTo(xRight, 80);
        contentStream.stroke();
        
        /*
        contentStream.moveTo(xMiddle, 100);
        contentStream.lineTo(xRight, 100);
        contentStream.stroke();
        */
    }

    private void drawFooterLabels() throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, 6);
        contentStream.setNonStrokingColor(darkBlue);

        String text = "Total mileage for month/period (Km)";
        float text_width = (PDType1Font.HELVETICA.getStringWidth(text) / 1000.0f) * 6;

        contentStream.beginText();
        contentStream.newLineAtOffset(xRight - 60 - text_width, 87);
        contentStream.showText(text);
        contentStream.endText();

        text = "Total cost for month/period (Euro)";
        text_width = (PDType1Font.HELVETICA.getStringWidth(text) / 1000.0f) * 6;

        contentStream.beginText();
        contentStream.newLineAtOffset(xRight - 60 - text_width, 67);
        contentStream.showText(text);
        contentStream.endText();

        /*
        text = "Reimbursement (0,17 Euro/Km)";
        text_width = (PDType1Font.HELVETICA.getStringWidth(text) / 1000.0f) * 6;

        contentStream.beginText();
        contentStream.newLineAtOffset(xRight - 60 - text_width, 67);
        contentStream.showText(text);
        contentStream.endText();
        */
    }

    private void drawHeaderValues() throws IOException {
        int monthlyDistance = 101;
        int totalDistance = 10101;
        int previousTotalDistance = totalDistance - monthlyDistance;
        if(previousTotalDistance < 0){
            previousTotalDistance = 0;
        }
        writeCellValue(0, 0, report.name);
        //writeCellValue(0, 1, "report.costCenter");
        //writeCellValue(0, 2, report.rhid);
        writeCellValue(1, 0, report.carModel);
        //writeCellValue(1, 1, "report.carId");
        //writeCellValue(1, 2, "" + Double.toString(report.mileageRate));
        writeCellValue(2, 0, "" + report.mileageRate);
        //writeCellValue(2, 1, "" + previousTotalDistance);
        //writeCellValue(2, 2, "" + totalDistance);
    }

    private void drawFooterValues() throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.setNonStrokingColor(darkBlue);

        String text = monthlyTotalMileage + ".0";
        float text_width = (PDType1Font.HELVETICA.getStringWidth(text) / 1000.0f) * 10;

        contentStream.beginText();
        contentStream.newLineAtOffset(xRight - 10 - text_width, 87);
        contentStream.showText(text);
        contentStream.endText();

        Locale italian = new Locale("it", "IT", "EURO");
        Locale.setDefault(italian);
        NumberFormat nf = NumberFormat.getCurrencyInstance();

        double cost = monthlyTotalMileage * report.mileageRate;
        //text = "" + cost;
        text = nf.format(cost);
        text_width = (PDType1Font.HELVETICA.getStringWidth(text) / 1000.0f) * 10;

        contentStream.setNonStrokingColor(darkRed);
        contentStream.beginText();
        contentStream.newLineAtOffset(xRight - 10 - text_width, 67);
        contentStream.showText(text);
        contentStream.endText();

    }

    private void prepareTable() throws IOException {
        int rows = report.trips.size();
        float wdt1 = 50; // date column width
        float wdt2 = 40; // km column width
        float wdt3 = 100; // purpose column width
        float wdt = 0.0f;
        float H = 20;
        //float startY = imageY - 125 - H;
        float startY = imageY - 45 - H;

        // drawing table header cells
        //contentStream.setNonStrokingColor(Color.DARK_GRAY);
        contentStream.setNonStrokingColor(darkRed);

        contentStream.addRect(xLeft, startY -H, wdt1, H);
        contentStream.fill();

        wdt = (xRight - xLeft) - (wdt1 + wdt2 + wdt3 + 3);
        contentStream.addRect(xLeft + wdt1 + 1, startY -H, wdt, H);
        contentStream.fill();

        contentStream.addRect(xRight - (wdt2 + 1) - wdt3, startY -H, wdt3, H);
        contentStream.fill();

        contentStream.addRect(xRight - wdt2, startY -20, wdt2, 20);
        contentStream.fill();
        // drawing table header cells
        // end ----------------------------------------------------------

        contentStream.setNonStrokingColor(Color.WHITE);

        float middleX1 = xLeft + (wdt1 / 2);
        float middleX2 = xRight - (wdt2 / 2);

        String text = "Date";
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
        float text_width = (PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000.0f) * 8;
        contentStream.newLineAtOffset(middleX1 - (text_width / 2), startY - 8);
        contentStream.showText(text);
        contentStream.endText();

        /*
        text = "From office to location";
        contentStream.beginText();
        text_width = (PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000.0f) * 8;
        contentStream.newLineAtOffset(x2 + (W / 2) - (text_width / 2), startY - 8);
        contentStream.showText(text);
        contentStream.endText();

        text = "Purpose and/or Customer";
        contentStream.beginText();
        text_width = (PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000.0f) * 8;
        contentStream.newLineAtOffset(x2 + (W * 1.5f) - (text_width / 2), startY - 8);
        contentStream.showText(text);
        contentStream.endText();
        */

        text = "FROM";
        contentStream.beginText();
        text_width = (PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000.0f) * 8;
        contentStream.newLineAtOffset(xLeft + (wdt1 + 1) + (wdt / 2) - (text_width / 2), startY - 8);
        contentStream.showText(text);
        contentStream.endText();
        text = "TO";
        contentStream.beginText();
        text_width = (PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000.0f) * 8;
        contentStream.newLineAtOffset(xLeft + (wdt1 + 1) + (wdt / 2) - (text_width / 2), startY - 18);
        contentStream.showText(text);
        contentStream.endText();


        text = "Purpose and/or";
        contentStream.beginText();
        text_width = (PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000.0f) * 8;
        contentStream.newLineAtOffset(xRight - (wdt2 + 1) - (wdt3 / 2) - (text_width / 2), startY - 8);
        contentStream.showText(text);
        contentStream.endText();
        text = "Customer";
        contentStream.beginText();
        text_width = (PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000.0f) * 8;
        contentStream.newLineAtOffset(xRight - (wdt2 + 1) - (wdt3 / 2) - (text_width / 2), startY - 18);
        contentStream.showText(text);
        contentStream.endText();

        text = "Mileage";
        contentStream.beginText();
        text_width = (PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000.0f) * 8;
        contentStream.newLineAtOffset(middleX2 - (text_width / 2), startY - 8);
        contentStream.showText(text);
        contentStream.endText();
        text = "(km)";
        contentStream.beginText();
        text_width = (PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000.0f) * 8;
        contentStream.newLineAtOffset(middleX2 - (text_width / 2), startY - 18);
        contentStream.showText(text);
        contentStream.endText();

        contentStream.setNonStrokingColor(darkBlue);
        contentStream.setLineDashPattern(new float[]{3,1}, 0);

        for (int i = 0; i < rows; i++){
            float baseY = startY - (H * 2) - (H * i);
            contentStream.moveTo(xLeft, baseY);
            contentStream.lineTo(xLeft + contentWidth, baseY);
            contentStream.stroke();

            if (isOdd(i)){
                contentStream.setNonStrokingColor(215/255f, 245/255f, 215/255f);
                contentStream.addRect(xLeft, baseY, contentWidth, H);
                contentStream.fill();
            }

            contentStream.setNonStrokingColor(darkBlue);
            contentStream.setFont(PDType1Font.HELVETICA, 6);
            text = "from:";
            contentStream.beginText();
            text_width = (PDType1Font.HELVETICA.getStringWidth(text) / 1000.0f) * 6;
            contentStream.newLineAtOffset(xLeft + (wdt1 + 1) + 25 - text_width, baseY + H - 8);
            contentStream.showText(text);
            contentStream.endText();
            text = "to:";
            contentStream.beginText();
            text_width = (PDType1Font.HELVETICA.getStringWidth(text) / 1000.0f) * 6;
            contentStream.newLineAtOffset(xLeft + (wdt1 + 1) + 25 - text_width, baseY + H - 18);
            contentStream.showText(text);
            contentStream.endText();
    
        }
    }

    private boolean isOdd(int number) {
        return number % 2 > 0;
    }

    private void writeTripLogs() throws IOException {
        Collections.sort(report.trips, new Comparator<Trip>() {
            @Override
            public int compare(Trip lhs, Trip rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.day > rhs.day ? 1 : (lhs.day < rhs.day) ? -1 : 0;
            }
        });

        float baseY = imageY - 85;
        for (Trip trip: report.trips) {
            baseY -= 20;
            writeLog(baseY, trip);
        }
    }

    private void writeLog(float baseY, Trip trip) throws IOException {
        contentStream.setNonStrokingColor(darkBlue);
        contentStream.setFont(PDType1Font.HELVETICA, 8);

        float xPurpose = xRight - 140;
        float xMileage = xRight - 10;
        float x2 = 120;
        float y = baseY + 8;

        contentStream.beginText();
        String text = trip.day + "/" + trip.month + "/" + trip.year;
        float text_width = (PDType1Font.HELVETICA.getStringWidth("" + text) / 1000.0f) * 8;
        contentStream.newLineAtOffset(xLeft + 25 - (text_width / 2), y);
        contentStream.showText(text);
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(xPurpose, y);
        contentStream.showText(trip.purpose);
        contentStream.endText();

        text = "" + trip.distance;
        contentStream.beginText();
        text_width = (PDType1Font.HELVETICA.getStringWidth(text) / 1000.0f) * 8;
        contentStream.newLineAtOffset(xMileage - text_width, y);
        contentStream.showText(text);
        contentStream.endText();

        monthlyTotalMileage += trip.distance;

        contentStream.setFont(PDType1Font.HELVETICA, 6);

        contentStream.beginText();
        contentStream.newLineAtOffset(x2, baseY + 12);
        contentStream.showText(trip.from);
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(x2, baseY + 2);
        contentStream.showText(trip.destination);
        contentStream.endText();


    }

}