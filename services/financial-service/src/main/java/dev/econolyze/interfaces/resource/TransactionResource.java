package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.PagedResponse;
import dev.econolyze.application.dto.request.TransactionRequest;
import dev.econolyze.application.dto.response.TransactionResponse;
import dev.econolyze.application.services.TransactionService;
import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.TransactionType;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestResponse;


@Path("/api/transaction")
public class TransactionResource {
    @Inject
    TransactionService transactionService;
    @POST
    public RestResponse<TransactionResponse> addTransaction(TransactionRequest request) {
        TransactionResponse response = transactionService.saveTransaction(request);
        return RestResponse.ok(response);
    }
    @GET
    public RestResponse<PagedResponse<TransactionResponse>> getAllTransactionsByUserId(@QueryParam("page") @DefaultValue("0") int page,
                                                                                       @QueryParam("pageSize") @DefaultValue("20") int pageSize) {
        PagedResponse<TransactionResponse> transactions = transactionService.getAllTransactionsByUserId(page, pageSize);
        return RestResponse.ok(transactions);
    }
    @GET
    @Path("/type/{transactionType}")
    public RestResponse<PagedResponse<TransactionResponse>> getAllTransactionsWithType(@PathParam("transactionType") TransactionType transactionType,
                                                                                    @QueryParam("page") @DefaultValue("0") int page,
                                                                                    @QueryParam("pageSize") @DefaultValue("20") int pageSize) {
        PagedResponse<TransactionResponse> transactions = transactionService.getAllTransactionsByUserIdAndType(transactionType, page, pageSize);
        return RestResponse.ok(transactions);
    }
    @GET
    @Path("/category/{category}")
    public RestResponse<PagedResponse<TransactionResponse>> getAllTransactionsWithTypeAndCategory(@PathParam("category")Category category,
                                                                                             @QueryParam("page") @DefaultValue("0") int page,
                                                                                             @QueryParam("pageSize") @DefaultValue("20") int pageSize){
        PagedResponse<TransactionResponse> transactions = transactionService.getTransactionByUserIdAndCategory(category, page, pageSize);
        return RestResponse.ok(transactions);
    }
    @GET
    @Path("/{id}")
    public RestResponse<TransactionResponse> getTransactionById(@PathParam("id") Long id) {
        TransactionResponse transactionResponse = transactionService.getTransactionById(id);
        return RestResponse.ok(transactionResponse);
    }
}
