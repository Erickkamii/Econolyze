package com.econolyze.dev.infrastructure.clients;

import com.econolyze.dev.interfaces.rest.dto.CdiRateDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

@RegisterRestClient(configKey = "financial-service")
@Path("/api/cdi/")
public interface CdiClient {
    @GET
    @Path("/rate")
    RestResponse<CdiRateDTO> getCdiRate(
            @HeaderParam("Authorization") String authorization
    );
}
