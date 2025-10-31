package dev.econolyze.client;

import dev.econolyze.dto.BalanceResponse;
import dev.econolyze.dto.PagedResponse;
import dev.econolyze.dto.TransactionRequest;
import dev.econolyze.dto.TransactionResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api")
@RegisterRestClient(configKey = "financial-service")
@RegisterClientHeaders
public interface FinancialServiceClient {

    @POST
    @Path("/transaction")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<TransactionResponse> createTransaction(
            @HeaderParam("X-User-Id") Long userId,
            TransactionRequest transaction
    );

    @GET
    @Path("/transaction")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<PagedResponse<TransactionResponse>> getTransactions(
            @HeaderParam("X-User-Id") Long userId
    );

    @GET
    @Path("/balance")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    RestResponse<BalanceResponse> getBalance(
        @HeaderParam("X-User-Id") Long userId
    );
}
