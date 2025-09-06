package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.FinancialGoalDTO;
import dev.econolyze.application.services.GoalService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path( "/api/goal")
public class GoalResource {

    @Inject
    GoalService goalService;

    @GET
    @Path("/all")
    public String getAll() {
        return "all goals";
    }

    @POST
    public Response createNewGoal(FinancialGoalDTO goalDTO) {
        FinancialGoalDTO createdGoal = goalService.createNewGoal(goalDTO);
        return Response.status(Response.Status.CREATED).entity(createdGoal).build();
    }
}
