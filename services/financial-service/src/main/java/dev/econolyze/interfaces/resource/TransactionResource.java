package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.services.TransactionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api/transaction")
public class TransactionResource {
    @Inject
    TransactionService transactionService;
    @POST
    public RestResponse<TransactionDTO> addTransaction(TransactionDTO transactionDto) {
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
