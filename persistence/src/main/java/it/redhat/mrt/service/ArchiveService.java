package it.redhat.mrt.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import it.redhat.mrt.model.ReportFileInfo;

@ApplicationScoped
public class ArchiveService {

    @ConfigProperty(name = "mrt.reports.dirname")
    String dirname;

    @Inject
    Logger logger;

    public File getPdf(String docName, String rhid) throws Exception {
        String[] parts = docName.substring(0, docName.length() - 4).split("_");

        if (parts.length != 4) {
            throw new Exception("Invalid file name " + docName);
        }

        File file = new File(dirname + File.separator + rhid + File.separator + parts[1] + File.separator + docName);

        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }

        return file;
    }

    public List<ReportFileInfo> getFileList(int year, String rhid) throws IOException {

        return findFiles(dirname + File.separator + rhid + File.separator + year);
    }

    public void delete(String docName, String rhid) throws Exception {
        logger.info("[ArchiveResource] about to delete: " + docName);
        String[] parts = docName.substring(0, docName.length() - 4).split("_");

        if (parts.length != 4) {
            throw new Exception("Invalid file name " + docName);
        }

        File file = new File(dirname + File.separator + rhid + File.separator + parts[1], docName);
        
        if (file.exists()) { // requester is also owner of the file
            file.delete();
            logger.info("[ArchiveResource] file: " + docName + " deleted.");
        }
    }

    private List<ReportFileInfo> findFiles(String path) throws IOException {

        List<ReportFileInfo> list = new ArrayList<ReportFileInfo>();

        File basedir = new File(path);
        if (!basedir.isDirectory()) {
            throw new IllegalArgumentException("path <" + path + "> is not a directory!");
        }

        File[] files = basedir.listFiles((dir, name) -> name.endsWith(".pdf"));
        for (File file : files) {
            
            Path p = Paths.get(file.getAbsolutePath());
            BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
            String[] parts = file.getName().substring(0, file.getName().length() - 4).split("_");

            if (parts.length != 4) {
                logger.warn("invalid file " + file.getAbsolutePath());
                continue;
            }

            int monthNumber = Integer.parseInt(parts[2]);
            int verNumber = Integer.parseInt(parts[3]);
            
            ReportFileInfo info = new ReportFileInfo();
            
            info.name = file.getName();
            info.creationTime = sdf.format(attr.creationTime().toMillis());
            info.size = "" + attr.size();
            info.month = Month.of(monthNumber).getDisplayName(TextStyle.FULL, Locale.getDefault());
            info.version = verNumber;

            list.add(info);
        }

        return list;
    }

}
