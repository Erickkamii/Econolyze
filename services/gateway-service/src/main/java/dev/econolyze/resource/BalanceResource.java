package dev.econolyze.resource;

import dev.econolyze.client.BalanceClient;
import dev.econolyze.dto.BalanceResponse;
import dev.econolyze.exception.ServiceUnavailableException;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.json.JsonNumber;
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
    private static final Logger LOG = Logger.getLogger(FinancialServiceResource.class);

    @Inject
    @RestClient
    BalanceClient balanceClient;

    @Inject
    JsonWebToken jwt;
    @GET
    @Path("/balance")
    public RestResponse<BalanceResponse> getBalance(@Context HttpHeaders headers) {
        String authorization = headers.getHeaderString("Authorization");
        JsonNumber userIdClaim = jwt.getClaim("userId");
        if (userIdClaim == null) {
            LOG.warn("JWT inválido: claim userId ausente");
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }

        Long userId = userIdClaim.longValueExact();

        try {
            return balanceClient.getBalance(authorization);
        } catch (Exception e) {
            LOG.errorf("Erro ao buscar saldo: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }
}
