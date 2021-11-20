package it.redhat.mrt.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import it.redhat.mrt.model.Key;

@Path("/key")
@Produces(MediaType.APPLICATION_JSON)
public class KeyResource {

    @Inject
    JsonWebToken accessToken;

    @ConfigProperty(name = "google.key") 
    String keyValue;

    @GET
    public Key get() {
        return new Key().setName("google.maps.api").setValue(keyValue);
    }

}
