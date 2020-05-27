package it.redhat.mrt.pdf.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import it.redhat.mrt.pdf.model.Associate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/associates")
@RegisterRestClient
public interface AssociateService {
    
    @GET
    @Path("/{rhid}")
    @Produces("application/json")
    Associate get(@PathParam String rhid);

}