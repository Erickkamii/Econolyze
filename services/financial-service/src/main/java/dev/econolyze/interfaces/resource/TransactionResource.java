package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.services.TransactionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestResponse;


@Path("/api/transaction")
public class TransactionResource {
    @Inject
    TransactionService transactionService;
    @POST
    public RestResponse<TransactionDTO> addTransaction(@HeaderParam("X-User-Id")Long userId,TransactionDTO transactionDto) {
        TransactionDTO transactionDTO = transactionService.saveTransaction(transactionDto);
        return RestResponse.ok(transactionDTO);
    }
    @GET
    @Path("/{userId}")
    public RestResponse<Iterable<TransactionDTO>> getAllTransactionsByUserId(@PathParam("userId") Long userId) {
        Iterable<TransactionDTO> transactions = transactionService.getAllTransactionsByUserId(userId);
        return RestResponse.ok(transactions);
    }
}
