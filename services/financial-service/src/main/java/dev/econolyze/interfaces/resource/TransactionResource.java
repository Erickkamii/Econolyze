package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.services.TransactionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/api/transaction")
public class TransactionResource {
    @Inject
    TransactionService transactionService;
    @POST
    public Response addTransaction(TransactionDTO transactionDto) {
        TransactionDTO transactionDTO = transactionService.saveTransaction(transactionDto);
        return Response.status(Response.Status.CREATED).entity(transactionDTO).build();
    }
    @GET
    @Path("/{userId}")
    public Response getAllTransactionsByUserId(@PathParam("userId") Long userId) {
        Iterable<TransactionDTO> transactions = transactionService.getAllTransactionsByUserId(userId);
        return Response.ok().entity(transactions).build();
    }
}
