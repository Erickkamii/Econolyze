package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.PagedResponse;
import dev.econolyze.application.dto.request.TransactionRequest;
import dev.econolyze.application.dto.request.TransactionUpdateRequest;
import dev.econolyze.application.dto.response.TransactionResponse;
import dev.econolyze.application.services.TransactionService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestResponse;


@Path("/api/transaction")
public class TransactionResource {
    @Inject
    TransactionService transactionService;

    @POST
    public Uni<RestResponse<TransactionResponse>> addTransaction(TransactionRequest request) {
        return transactionService.saveTransaction(request)
                .map(RestResponse::ok)
                .onFailure().recoverWithItem(RestResponse.serverError());
    }

    @PUT
    @Path("/{id}")
    public Uni<RestResponse<TransactionResponse>> updateTransaction(@PathParam("id") Long id, TransactionUpdateRequest request) {
        return transactionService.updateTransaction(id, request)
                .map(RestResponse::ok)
                .onFailure().recoverWithItem(RestResponse.serverError());
    }

    @GET
    public Uni<RestResponse<PagedResponse<TransactionResponse>>> getAllTransactionsByUserId(@QueryParam("page") @DefaultValue("0") int page,
                                                                                       @QueryParam("pageSize") @DefaultValue("20") int pageSize,
                                                                                       @QueryParam("sortBy") @DefaultValue("id") String sortBy,
                                                                                       @QueryParam("sortDir") @DefaultValue("DESC") String sortDir,
                                                                                       @QueryParam("type") String type,
                                                                                       @QueryParam("category") String category) {
        return transactionService.getAllTransactionsByUserId(page, pageSize, sortBy, sortDir, type, category)
                .map(RestResponse::ok)
                .onFailure().recoverWithItem(RestResponse.serverError());
    }

    @GET
    @Path("/{id}")
    public Uni<RestResponse<TransactionResponse>> getTransactionById(@PathParam("id") Long id) {
        return transactionService.getTransactionById(id).map(RestResponse::ok)
                .onFailure().recoverWithItem(RestResponse.notFound());
    }
}
