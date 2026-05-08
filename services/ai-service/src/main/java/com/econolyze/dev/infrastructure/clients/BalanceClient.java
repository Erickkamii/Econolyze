package com.econolyze.dev.infrastructure.clients;

import com.econolyze.dev.interfaces.rest.dto.BalanceResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

@RegisterRestClient(configKey = "financial-service")
@Path("/api/balance")
public interface BalanceClient {

    @GET
    Uni<RestResponse<BalanceResponse>> getBalance(
            @HeaderParam("Authorization") String token
    );
}
