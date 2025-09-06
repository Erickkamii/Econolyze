package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.IncomeDTO;
import dev.econolyze.application.services.IncomeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/income")
public class IncomeResource {
    @Inject
    IncomeService incomeService;

    @GET
    @Path("{userId}")
    public Response getAll(@PathParam("userId") Long userId) {
        List<IncomeDTO> incomes = incomeService.getIncomesByUserId(userId);
        return Response.ok().entity(incomes).build();
    }
}
