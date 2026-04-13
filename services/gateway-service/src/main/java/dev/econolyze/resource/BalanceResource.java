package dev.econolyze.resource;

import dev.econolyze.client.BalanceClient;
import dev.econolyze.dto.response.BalanceResponse;
import dev.econolyze.exception.ServiceUnavailableException;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/account")
@Authenticated
public class BalanceResource {
    private static final Logger LOG = Logger.getLogger(TransactionResource.class);

    @Inject
    @RestClient
    BalanceClient balanceClient;

    @Inject
    JsonWebToken jwt;
    @GET
    @Path("/balance")
    public Uni<RestResponse<BalanceResponse>> getBalance(@Context HttpHeaders headers) {
        if(jwt.getClaim("userId") == null) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return balanceClient.getBalance(headers.getHeaderString("Authorization"))
                .onFailure().invoke(e -> LOG.errorf("Erro ao buscar saldo: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }
}
