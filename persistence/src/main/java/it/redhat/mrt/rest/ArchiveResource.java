package it.redhat.mrt.rest;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.oidc.IdToken;
import it.redhat.mrt.model.ReportFileInfo;

@Path("/archive")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ArchiveResource {
    
    @Inject
    @IdToken
    JsonWebToken idToken;

    @ConfigProperty(name = "mrt.reports.dirname") 
    String dirname;

    @GET
    @Path("/pdf/{doc}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public File getPdf(@PathParam("doc") String docName) {
        String[] parts = docName.substring(0, docName.length() - 4).split("_");
        File file = new File(dirname + "/" + getRhid() + "/" + parts[1] + "/" + docName);
        return file;
    }

    @GET
    @Path("/{year}")
    public List<ReportFileInfo> getFileList(@PathParam("year") int year) {

        List<ReportFileInfo> list;
        try {
            list = ReportFileInfo.findFiles(dirname + "/" + getRhid() + "/" + year);
        } catch (IOException e) {
            e.printStackTrace();
            list = new ArrayList<ReportFileInfo>();
        }
        return list;
    }

    private String getRhid(){
        String rhid = null;
        Object rhidObject = idToken.getClaim("rhid");
        if (rhidObject != null) {
            rhid = rhidObject.toString();
        }
        return rhid;
    }
}
