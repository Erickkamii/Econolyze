package dev.econolyze.resource;

import dev.econolyze.aggregator.TransactionDependenciesAggregator;
import dev.econolyze.client.TransactionClient;
import dev.econolyze.dto.flter.TransactionFilter;
import dev.econolyze.dto.request.UpdateTransactionRequest;
import dev.econolyze.dto.response.PagedResponse;
import dev.econolyze.dto.request.TransactionRequest;
import dev.econolyze.dto.response.TransactionDependenciesResponse;
import dev.econolyze.dto.response.TransactionResponse;
import dev.econolyze.exception.ServiceUnavailableException;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;


@Path("/api")
@Authenticated
public class TransactionResource {

    private static final Logger LOG = Logger.getLogger(TransactionResource.class);

    @Inject
    @RestClient
    TransactionClient transactionClient;
    @Inject
    TransactionDependenciesAggregator transactionDependenciesAggregator;
    @Inject
    JsonWebToken jwt;

    private boolean unauthorized(){
        return jwt.getClaim("userId") == null;
    }

    @GET
    @Path("/dependencies")
    public Uni<RestResponse<TransactionDependenciesResponse>> getDependencies(@Context HttpHeaders headers){
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return transactionDependenciesAggregator.load(headers.getHeaderString("Authorization"))
                .map(RestResponse::ok)
                .onFailure().invoke(e -> LOG.errorf("Erro ao buscar as dependencias: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @POST
    @Path("/transaction")
    public Uni<RestResponse<TransactionResponse>> transaction(@Context HttpHeaders headers, TransactionRequest request){
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return transactionClient.createTransaction(headers.getHeaderString("Authorization"), request)
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar transação: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @PUT
    @Path("/transaction/{id}")
    public Uni<RestResponse<TransactionResponse>> updateTransaction(@Context HttpHeaders headers, @PathParam("id") Long id, UpdateTransactionRequest request) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return transactionClient.updateTransaction(headers.getHeaderString("Authorization"), id, request)
                .onFailure().invoke(e -> LOG.errorf("Erro ao atualizar transação: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/transaction")
    public Uni<RestResponse<PagedResponse<TransactionResponse>>> getTransactions(
            @Context HttpHeaders headers,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @BeanParam TransactionFilter filter
    ) {
        if(unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return transactionClient.getTransactions(headers.getHeaderString("Authorization"), page, size, filter.type(), filter.category())
                .onFailure().invoke(e -> LOG.errorf("Erro ao buscar transações: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/transaction/{id}")
    public Uni<RestResponse<TransactionResponse>> getTransactionById(
            @Context HttpHeaders headers,
            @PathParam("id") Long id
    ) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return transactionClient.getTransactionById(headers.getHeaderString("Authorization"), id)
                .onFailure().invoke(e -> LOG.errorf("Erro ao buscar transação por ID): %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }
}
