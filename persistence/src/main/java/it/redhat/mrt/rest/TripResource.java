package it.redhat.mrt.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.NotFoundException;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.oidc.IdToken;
import it.redhat.mrt.model.Trip;

@Path("/trips")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TripResource {
    
    @Inject
    @IdToken
    JsonWebToken idToken;

    @Inject
    JsonWebToken accessToken;

    @GET
    public List<PanacheMongoEntityBase> list() {
        return Trip.listAll();
    }

    @GET
    @Path("{year}/{month}")
    public List<Trip> get(@PathParam("year") int year, @PathParam("month") int month) {
        return Trip.getTrips(getRhid(), year, month);
    }

    @GET
    @Path("/{id}")
    public Trip get(@PathParam("id") String id) {
        return Trip.findById(new ObjectId(id));
    }

    @POST
    public Trip create(Trip trip) {
        trip.rhid = getRhid();
        trip.persist();
        return trip;
    }

    @PUT
    @Path("/{id}")
    public void update(@PathParam("id") String id, Trip trip) {
        trip.update();
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        Trip trip = Trip.findById(new ObjectId(id));
        if(trip == null) {
            throw new NotFoundException();
        }
        trip.delete();
    }

    @GET
    @Path("/count")
    public Long count() {
        return Trip.count();
    }    

    private String getRhid(){
        String rhid = "undefined";
        Object rhidObject = idToken.getClaim("rhid");
        if (rhidObject != null) {
            rhid = rhidObject.toString();
        }    
        return rhid;
    }
}