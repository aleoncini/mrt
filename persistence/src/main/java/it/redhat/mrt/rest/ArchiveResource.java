package it.redhat.mrt.rest;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.oidc.IdToken;
import it.redhat.mrt.model.ReportFileInfo;
import it.redhat.mrt.service.ArchiveService;

@Path("/archive")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ArchiveResource {

    @Inject
    @IdToken
    JsonWebToken idToken;

    @Inject
    ArchiveService archiveService;

    @GET
    @Path("/pdf/{doc}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public File getPdf(@PathParam("doc") String docName) {
        return archiveService.getPdf(docName, getRhidFromJWT());
    }

    @GET
    @Path("/{year}")
    public List<ReportFileInfo> getFileList(@PathParam("year") int year) {

        return archiveService.getFileList(year, getRhidFromJWT());

    }

    @DELETE
    @Path("/{doc}")
    public void delete(@PathParam("doc") String docName) {

        archiveService.delete(docName, getRhidFromJWT());

    }

    private String getRhidFromJWT() {
        String rhid = null;
        Object rhidObject = idToken.getClaim("rhid");
        if (rhidObject != null) {
            rhid = rhidObject.toString();
        }
        return rhid;
    }

}
