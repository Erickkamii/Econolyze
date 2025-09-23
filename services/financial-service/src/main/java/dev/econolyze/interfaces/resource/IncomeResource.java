package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.IncomeDTO;
import dev.econolyze.application.services.IncomeService;
import dev.econolyze.domain.enums.Category;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;

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

    @GET
    @Path("/{userId}/{category}")
    public RestResponse<List<IncomeDTO>> getAllByCategory(@PathParam("userId") Long userId, @PathParam("category") String category) {
        List<IncomeDTO> incomes = incomeService.getIncomesByUserIdAndCategory(userId, category);
        return RestResponse.ok(incomes);
    }
}
