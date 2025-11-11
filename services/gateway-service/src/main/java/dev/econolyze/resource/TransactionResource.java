package dev.econolyze.resource;

import dev.econolyze.client.TransactionClient;
import dev.econolyze.dto.response.PagedResponse;
import dev.econolyze.dto.request.TransactionRequest;
import dev.econolyze.dto.response.TransactionResponse;
import dev.econolyze.exception.ServiceUnavailableException;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.json.JsonNumber;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;


@Path("/api/financial")
@Authenticated
public class TransactionResource {

    private static final Logger LOG = Logger.getLogger(TransactionResource.class);

    @Inject
    @RestClient
    TransactionClient transactionClient;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/transaction")
    public RestResponse<TransactionResponse> transaction(@Context HttpHeaders headers, TransactionRequest request){
        JsonNumber userIdClaim = jwt.getClaim("userId");
        String authorization = headers.getHeaderString("Authorization");
        if (userIdClaim == null) {
            throw new ServiceUnavailableException("financial-service");
        }
        try {
            return transactionClient.createTransaction(authorization, request);
        } catch (Exception e) {
            LOG.errorf("Erro ao criar transação: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @GET
    @Path("/transactions")
    public RestResponse<PagedResponse<TransactionResponse>> getTransactions(
            @Context HttpHeaders headers,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size
    ){
        JsonNumber userId = jwt.getClaim("userId");
        if (userId == null) {
            LOG.warn("JWT inválido: claim userId ausente");
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        String authorization = headers.getHeaderString("Authorization");
        try{
            return transactionClient.getTransactions(authorization, page, size);
        } catch (Exception e){
            LOG.errorf("Erro ao buscar as transações: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @GET
    @Path("/transactions/type/{transactionType}")
    public RestResponse<PagedResponse<TransactionResponse>> getAllTransactionsWithType(
            @Context HttpHeaders headers,
            @PathParam("transactionType") String transactionType,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size
    ){
        String authorization = headers.getHeaderString("Authorization");
        if (jwt.getClaim("userId") == null){
            LOG.warn("JWT inválido: claim userId ausente");
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        try{
            return transactionClient.getTransactionsByType(authorization, transactionType, page, size);
        } catch (Exception e){
            LOG.errorf("Erro ao buscar as transações: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @GET
    @Path("/transactions/category/{category}")
    public RestResponse<PagedResponse<TransactionResponse>> getAllTransactionsWithCategory(
            @Context HttpHeaders headers,
            @PathParam("category") String category,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size
    ){
        String authorization = headers.getHeaderString("Authorization");
        if (jwt.getClaim("userId") == null){
            LOG.warn("JWT inválido: claim userId ausente");
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        try{
            return transactionClient.getTransactionsByCategory(authorization, category, page, size);
        } catch (Exception e){
            LOG.errorf("Erro ao buscar as transações: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }
    @GET
    @Path("/transactions/{id}")
    public RestResponse<TransactionResponse> getTransactionById(
            @Context HttpHeaders headers,
            @PathParam("id") Long id
    ) {
        String authorization = headers.getHeaderString("Authorization");

        if (jwt.getClaim("userId") == null) {
            LOG.warn("JWT inválido: claim userId ausente");
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        try {
            return transactionClient.getTransactionById(authorization, id);
        } catch (Exception e) {
            LOG.errorf("Erro ao buscar as transações: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }
}
