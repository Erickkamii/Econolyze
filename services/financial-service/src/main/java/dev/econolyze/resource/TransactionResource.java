package dev.econolyze.resource;

import dev.econolyze.dto.TransactionDTO;
import dev.econolyze.services.TransactionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

@Path("/api/transaction")
public class TransactionResource {
    @Inject
    TransactionService transactionService;
    @POST
    public TransactionDTO addTransaction(TransactionDTO transactionDto) {
        return transactionService.saveTransaction(transactionDto);
    }
    @GET
    @Path("/{userId}")
    public Iterable<TransactionDTO> getAllTransactionsByUserId(@PathParam("userId") Long userId) {
        return transactionService.getAllTransactionsByUserId(userId);
    }
}
