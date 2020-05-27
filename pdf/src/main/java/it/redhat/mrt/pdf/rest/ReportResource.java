package it.redhat.mrt.pdf.rest;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import it.redhat.mrt.pdf.builder.PdfBuilder;
import it.redhat.mrt.pdf.model.Report;
import it.redhat.mrt.pdf.service.AssociateService;
import it.redhat.mrt.pdf.service.TripService;

@Produces(MediaType.APPLICATION_JSON)
@Path("/reports")
public class ReportResource {
    @ConfigProperty(name = "report.directory")
    String reportDirectory;

    @Inject
    @RestClient
    AssociateService aService;

    @Inject
    @RestClient
    TripService tService;

    @GET
    public String test() {
        return new String("hello");
    }

    @POST
    @Path("/build/{rhid}/{year}/{month}")
    public String build(@PathParam("rhid") String rhid,@PathParam("year") int year,@PathParam("month") int month) {
        CompletableFuture.runAsync(() -> {
            Report report = new Report().setAssociate(aService.get(rhid))
                .setYear(year)
                .setMonth(month)
                .setTrips(tService.get(rhid, year, month));
            new PdfBuilder()
                .setReport(report)
                .setReportDirectory(reportDirectory)
                .build();
        });
        return new String("{\"result\": \"Build successfully scheduled\"}");
    }
}