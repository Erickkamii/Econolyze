package dev.econolyze.interfaces.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
public class GreetingResource {

    @GET
    @Path("/ping")
    public Response hello() {
        return Response.ok().entity("Pong!").build();
    }
}
