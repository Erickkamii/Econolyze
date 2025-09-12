package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.FinancialGoalDTO;
import dev.econolyze.application.dto.GoalProgressDTO;
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

    @GET
    @Path("/{goalId}")
    public Response getGoalById(@PathParam("goalId") Long goalId) {
        FinancialGoalDTO goalDTO = goalService.getGoalById(goalId);
        return Response.ok().entity(goalDTO).build();
    }

    @GET
    @Path("/progress/{goalId}")
    public Response getGoalProgress(@PathParam("goalId") Long goalId) {
        Long userId = 1L;
        GoalProgressDTO progressDTO = goalService.getGoalProgress(goalId, userId);
        return Response.ok().entity(progressDTO).build();
    }

    @POST
    public Response createNewGoal(FinancialGoalDTO goalDTO) {
        FinancialGoalDTO createdGoal = goalService.createNewGoal(goalDTO);
        return Response.status(Response.Status.CREATED).entity(createdGoal).build();
    }
}
