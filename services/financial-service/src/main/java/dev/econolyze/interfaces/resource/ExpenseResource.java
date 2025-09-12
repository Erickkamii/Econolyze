package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.ExpenseDTO;
import dev.econolyze.application.services.ExpenseService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/expense")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseResource {

    @Inject
    ExpenseService expenseService;

    @GET
    @Path("{userId}")
    public Response getAll(@PathParam("userId") Long userId) {
        List<ExpenseDTO> expenses = expenseService.getAllExpensesByUserId(userId);
        return Response.ok().entity(expenses).build();
    }
}
