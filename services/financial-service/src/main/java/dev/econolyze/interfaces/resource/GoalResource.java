package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.FinancialGoalDTO;
import dev.econolyze.application.dto.GoalProgressDTO;
import dev.econolyze.application.services.GoalService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;

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
    public RestResponse<FinancialGoalDTO> getGoalById(@PathParam("goalId") Long goalId) {
        FinancialGoalDTO goalDTO = goalService.getGoalById(goalId);
        return RestResponse.ok(goalDTO);
    }

    @GET
    @Path("/progress/{goalId}")
    public RestResponse<GoalProgressDTO> getGoalProgress(@PathParam("goalId") Long goalId) {
        Long userId = 1L;
        GoalProgressDTO progressDTO = goalService.getGoalProgress(goalId, userId);
        return RestResponse.ok(progressDTO);
    }

    @POST
    public RestResponse<FinancialGoalDTO> createNewGoal(FinancialGoalDTO goalDTO) {
        FinancialGoalDTO createdGoal = goalService.createNewGoal(goalDTO);
        return RestResponse.ok(createdGoal);
    }
}
