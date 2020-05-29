package it.redhat.mrt.pdf.rest;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.redhat.mrt.pdf.builder.PdfBuilder;
import it.redhat.mrt.pdf.model.Report;
import it.redhat.mrt.pdf.model.Trip;
import it.redhat.mrt.pdf.service.AssociateService;
import it.redhat.mrt.pdf.service.TripService;

@Produces(MediaType.APPLICATION_JSON)
@Path("/reports")
public class ReportResource {
    private static final Logger logger = LoggerFactory.getLogger("it.redhat.mrt");

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
        logger.info("[ReportResource] scheduling build for user with rhid: " + rhid);
        CompletableFuture.runAsync(() -> {
            Report report = new Report().setAssociate(aService.get(rhid))
                .setYear(year)
                .setMonth(month)
                .setTrips(tService.getMonthlyTrips(rhid, year, month));
            new PdfBuilder()
                .setReport(report)
                .build();
        });
        return new String("{\"result\": \"Build successfully scheduled\"}");
    }
}