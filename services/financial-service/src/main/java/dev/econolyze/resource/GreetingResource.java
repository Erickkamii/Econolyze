package dev.econolyze.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api")
public class GreetingResource {

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "pong!";
    }
}
