package it.redhat.mrt.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.redhat.mrt.model.ReportFileInfo;

@Path("/archive")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ArchiveResource {
    
    @ConfigProperty(name = "mrt.reports.dirname") 
    String dirname;

    private static final Logger logger = LoggerFactory.getLogger("it.redhat.mrt");

    @GET
    @Path("/pdf/{rhid}/{doc}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public File getPdf(@PathParam("doc") String docName, @PathParam("rhid") String rhid) {
        String[] parts = docName.substring(0, docName.length() - 4).split("_");
        File file = new File(dirname + "/" + rhid + "/" + parts[1] + "/" + docName);
        return file;
    }

    @GET
    @Path("/{rhid}/{year}")
    public List<ReportFileInfo> getFileList(@PathParam("rhid") String rhid, @PathParam("year") int year) {

        List<ReportFileInfo> list = new ArrayList<ReportFileInfo>();
        try {
            list = ReportFileInfo.findFiles(dirname + "/" + rhid + "/" + year);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @DELETE
    @Path("/{rhid}/{doc}")
    public void delete(@PathParam("rhid") String rhid, @PathParam("doc") String docName) {
        logger.info("[ArchiveResource] about to delete: " + docName);
        String[] parts = docName.substring(0, docName.length() - 4).split("_");
        File file = new File(dirname + "/" + rhid + "/" + parts[1], docName);
        if(file.exists()){  // requester is also owner of the file
            file.delete();
            logger.info("[ArchiveResource] file: " + docName + " deleted.");
        }
    }

}
