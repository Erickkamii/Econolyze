package dev.econolyze.resource;

import dev.econolyze.client.FinancialServiceClient;
import dev.econolyze.dto.BalanceResponse;
import dev.econolyze.dto.TransactionRequest;
import dev.econolyze.dto.TransactionResponse;
import dev.econolyze.exception.ServiceUnavailableException;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.json.JsonNumber;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;


@Path("/api/financial")
@Authenticated
public class FinancialServiceResource {

    private static final Logger LOG = Logger.getLogger(FinancialServiceResource.class);

    @Inject
    @RestClient
    FinancialServiceClient financialServiceClient;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/balance")
    public RestResponse<BalanceResponse> getBalance() {
        JsonNumber userIdClaim = jwt.getClaim("userId");
        if (userIdClaim == null) {
            LOG.warn("JWT inválido: claim userId ausente");
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }

        Long userId = userIdClaim.longValueExact();

        try {
            return financialServiceClient.getBalance(userId);
        } catch (Exception e) {
            LOG.errorf("Erro ao buscar saldo: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @POST
    @Path("/transaction")
    public RestResponse<TransactionResponse> transaction(TransactionRequest request){
        JsonNumber userIdClaim = jwt.getClaim("userId");
        if (userIdClaim == null) {
            throw new ServiceUnavailableException("financial-service");
        }
        Long userId = userIdClaim.longValueExact();
        try {
            return financialServiceClient.createTransaction(userId, request);
        } catch (Exception e) {
            LOG.errorf("Erro ao criar transação: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }
}
