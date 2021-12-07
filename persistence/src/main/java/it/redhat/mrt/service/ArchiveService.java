package it.redhat.mrt.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    public File getPdf(String docName, String rhid) {
        String[] parts = docName.substring(0, docName.length() - 4).split("_");
        File file = new File(dirname + "/" + rhid + "/" + parts[1] + "/" + docName);
        return file;
    }

    public List<ReportFileInfo> getFileList(int year, String rhid) {

        List<ReportFileInfo> list = new ArrayList<ReportFileInfo>();
        try {
            list = ReportFileInfo.findFiles(dirname + "/" + rhid + "/" + year);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void delete(String docName, String rhid) {
        logger.info("[ArchiveResource] about to delete: " + docName);
        String[] parts = docName.substring(0, docName.length() - 4).split("_");
        File file = new File(dirname + "/" + rhid + "/" + parts[1], docName);
        if (file.exists()) { // requester is also owner of the file
            file.delete();
            logger.info("[ArchiveResource] file: " + docName + " deleted.");
        }
    }
}
