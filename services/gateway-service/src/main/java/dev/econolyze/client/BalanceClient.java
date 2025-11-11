package dev.econolyze.client;

import dev.econolyze.dto.response.BalanceResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api")
@RegisterRestClient(configKey = "financial-service")
@RegisterClientHeaders(AuthHeaderFactory.class)
public interface BalanceClient {
    @GET
    @Path("/balance")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    RestResponse<BalanceResponse> getBalance(
            @HeaderParam("Authorization") String authorization
    );
}
