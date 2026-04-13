package dev.econolyze.client;

import dev.econolyze.dto.response.PagedResponse;
import dev.econolyze.dto.request.TransactionRequest;
import dev.econolyze.dto.response.TransactionResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api")
@RegisterRestClient(configKey = "financial-service")
@RegisterClientHeaders(AuthHeaderFactory.class)
public interface TransactionClient {

    @POST
    @Path("/transaction")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<TransactionResponse>> createTransaction(
            @HeaderParam("Authorization") String authorization,
            TransactionRequest transaction
    );

    @GET
    @Path("/transaction")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<PagedResponse<TransactionResponse>>> getTransactions(
            @HeaderParam("Authorization") String authorization,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize,
            @QueryParam("type") String type,
            @QueryParam("category") String category
    );

    @GET
    @Path("/transaction/{id}")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<TransactionResponse>> getTransactionById(
            @HeaderParam("Authorization") String authorization,
            @PathParam("id") Long id
    );
}
