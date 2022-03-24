package it.redhat.mrt.rest;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.redhat.mrt.model.Report;
import it.redhat.mrt.pdf.Builder;

@Path("/reports")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReportResource {

    @Inject
    JsonWebToken token;
    
    private static final Logger logger = LoggerFactory.getLogger("it.redhat.mrt");

    @ConfigProperty(name = "mrt.reports.dirname") 
    String dirname;

    @POST
    @Path("/{userid}/{year}/{month}")
    public String build(@PathParam("userid") String userid,@PathParam("year") int year,@PathParam("month") int month) {

        userid = token.getClaim("sub");

        Report report = Report.build(userid, year, month);
        CompletableFuture.runAsync(() -> {
            logger.info("[ReportResource] running async, creating PDF Report.");
            new Builder().setBaseDir(dirname).setReport(report).build();
        });
        return new String("{\"result\": \"Build successfully scheduled\"}");
    }

}
