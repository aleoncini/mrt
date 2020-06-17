package it.redhat.mrt.pdf.rest;

import java.io.File;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.redhat.mrt.pdf.builder.PdfBuilder;
import it.redhat.mrt.pdf.model.Associate;
import it.redhat.mrt.pdf.model.Report;
import it.redhat.mrt.pdf.model.Trip;
import it.redhat.mrt.pdf.service.AssociateService;
import it.redhat.mrt.pdf.service.TripService;

@Produces(MediaType.APPLICATION_JSON)
@Path("/reports")
public class ReportResource {

    private static final Logger logger = LoggerFactory.getLogger("it.redhat.mrt");

    private static final String REPORTNAME_PATTERN = ".+_\\d\\d\\d\\d_\\d\\d";

    private final Pattern pattern;

    @Inject
    @RestClient
    AssociateService aService;

    @Inject
    @RestClient
    TripService tService;

    public ReportResource() {
    	pattern = Pattern.compile(REPORTNAME_PATTERN);
	}

    @GET
    @Path("{reportname}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    /**
     * Returns a report with a specified report name in the form RHID_YYYY_MM
     */
    public Response getReportByName(@PathParam("reportname") String reportname) {

    	Matcher matcher = pattern.matcher(reportname);

    	if (!matcher.matches()) {
			throw new WebApplicationException(
					Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("reportname is not valid").build());
		}

    	String filename = PdfBuilder.reportDirectory + File.separator + reportname + ".pdf";

    	File fileDownload = new File(filename);

    	if (!fileDownload.exists()) {
    		return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("report requested doesn't exists").build();
    	}

    	ResponseBuilder response = Response.ok((Object) fileDownload);
        response.header("Content-Disposition", "attachment;filename=" + filename);
        return response.build();

    }
    
    @POST
    @Path("/build/{rhid}/{year}/{month}")
    public String build(@PathParam("rhid") String rhid,@PathParam("year") int year,@PathParam("month") int month) {
        logger.info("[ReportResource] scheduling build for user with rhid: " + rhid);
        CompletableFuture.runAsync(() -> {
            logger.info("[ReportResource] running async...");
            Associate associate = aService.get(rhid);
            logger.info("[ReportResource] async, got associate: " + associate.getName());
            Set<Trip> trips = tService.getMonthlyTrips(rhid, year, month);
            logger.info("[ReportResource] async, got trips: " + trips.size());
            Report report = new Report().setAssociate(associate)
                .setYear(year)
                .setMonth(month)
                .setTrips(trips);
            new PdfBuilder()
                .setReport(report)
                .build();
        });
        return new String("{\"result\": \"Build successfully scheduled\"}");
    }
}