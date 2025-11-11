package dev.econolyze.client;

import dev.econolyze.dto.response.PagedResponse;
import dev.econolyze.dto.request.TransactionRequest;
import dev.econolyze.dto.response.TransactionResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<TransactionResponse> createTransaction(
            @HeaderParam("Authorization") String authorization,
            TransactionRequest transaction
    );

    @GET
    @Path("/transaction")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<PagedResponse<TransactionResponse>> getTransactions(
            @HeaderParam("Authorization") String authorization,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    );

    @GET
    @Path("/transaction/type/{transactionType}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<PagedResponse<TransactionResponse>> getTransactionsByType(
            @HeaderParam("Authorization") String authorization,
            @PathParam("transactionType") String transactionType,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    );

    @GET
    @Path("/transaction/category/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<PagedResponse<TransactionResponse>> getTransactionsByCategory(
            @HeaderParam("Authorization") String authorization,
            @PathParam("category") String category,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    );

    @GET
    @Path("/transaction/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<TransactionResponse> getTransactionById(
            @HeaderParam("Authorization") String authorization,
            @PathParam("id") Long id
    );
}
