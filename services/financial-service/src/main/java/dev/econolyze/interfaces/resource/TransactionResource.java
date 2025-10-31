package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.PagedResponse;
import dev.econolyze.application.dto.TransactionDTO;
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
    public RestResponse<TransactionDTO> addTransaction(@HeaderParam("X-User-Id")Long userId,TransactionDTO transactionDto) {
        TransactionDTO transactionDTO = transactionService.saveTransaction(transactionDto, userId);
        return RestResponse.ok(transactionDTO);
    }
    @GET
    public RestResponse<PagedResponse<TransactionDTO>> getAllTransactionsByUserId(@HeaderParam("X-User-Id") Long userId,
                                                                                  @QueryParam("page") @DefaultValue("0") int page,
                                                                                  @QueryParam("pageSize") @DefaultValue("20") int pageSize) {
        PagedResponse<TransactionDTO> transactions = transactionService.getAllTransactionsByUserId(userId, page, pageSize);
        return RestResponse.ok(transactions);
    }
    @GET
    @Path("/{transactionType}")
    public RestResponse<PagedResponse<TransactionDTO>> getAllTransactionsWithType(@HeaderParam("X-User-Id") Long userId,
                                                          @PathParam("transactionType") TransactionType transactionType,
                                                          @QueryParam("page") @DefaultValue("0") int page,
                                                          @QueryParam("pageSize") @DefaultValue("20") int pageSize) {
        PagedResponse<TransactionDTO> transactions = transactionService.getAllTransactionsByUserIdAndType(userId, transactionType, page, pageSize);
        return RestResponse.ok(transactions);
    }
    @GET
    @Path("/{category}")
    public RestResponse<PagedResponse<TransactionDTO>> getAllTransactionsWithTypeAndCategory(@HeaderParam("X-User-Id") Long userId,
                                                                                             @PathParam("category")Category category,
                                                                                             @QueryParam("page") @DefaultValue("0") int page,
                                                                                             @QueryParam("pageSize") @DefaultValue("20") int pageSize){
        PagedResponse<TransactionDTO> transactions = transactionService.getTransactionByUserIdAndCategory(userId, category, page, pageSize);
        return RestResponse.ok(transactions);
    }
}
