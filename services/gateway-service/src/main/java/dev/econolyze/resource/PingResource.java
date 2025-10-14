package dev.econolyze.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/ping")
public class PingResource {
    @GET
    public RestResponse<String> ping() {
        return RestResponse.ok("pong");
    }
}
// ...existing code...

