package it.redhat.mrt.backend.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.redhat.mrt.backend.model.Associate;
import it.redhat.mrt.backend.service.AssociateService;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/associates")
public class AssociateResource {
    @Inject AssociateService service;

    @GET
    public List<Associate> list() {
        return service.list();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String id) {

    	Associate associate = service.get(id);

        if (associate == null) {
        	return Response.status(Response.Status.NOT_FOUND).entity("Associate not found for id " + id).build();
        }

        return Response.ok(associate).build();
    }

    @POST
    public List<Associate> add(Associate associate) {
        service.add(associate);
        return list();
    }

}