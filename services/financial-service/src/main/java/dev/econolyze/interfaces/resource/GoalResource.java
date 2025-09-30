package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.FinancialGoalDTO;
import dev.econolyze.application.dto.GoalProgressDTO;
import dev.econolyze.application.dto.InvestmentProjectionDTO;
import dev.econolyze.application.services.GoalService;
import dev.econolyze.application.services.InvestmentService;
import dev.econolyze.domain.enums.Estimate;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;

import java.math.BigDecimal;

@Path( "/api/goal")
public class GoalResource {

    @Inject
    GoalService goalService;
    @Inject
    InvestmentService investmentService;

    InvestmentProjectionDTO projectionDTO;

    @POST
    public RestResponse<FinancialGoalDTO> createNewGoal(FinancialGoalDTO goalDTO) {
        FinancialGoalDTO createdGoal = goalService.createNewGoal(goalDTO);
        return RestResponse.ok(createdGoal);
    }

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

    @GET
    @Path("/annual-cdi/{userId}")
    public RestResponse<InvestmentProjectionDTO> getAnnualInvestmentProgress(@PathParam("userId") Long userId,
                                                                             @RestQuery("percentage") BigDecimal percentage){
        if ( percentage == null)
            projectionDTO = investmentService.getProjectionBasedOnCdiRate(userId, Estimate.YEARLY);
        else
            projectionDTO = investmentService.getProjectionBasedOnCdiWithPercentage(userId, Estimate.YEARLY, percentage);
        return RestResponse.ok(projectionDTO);
    }

    @GET
    @Path("/monthly-cdi/{userId}")
    public RestResponse<InvestmentProjectionDTO> getMonthlyInvestmentProgress(@PathParam("userId") Long userId,
                                                                              @RestQuery("percentage") BigDecimal percentage) {
        if ( percentage == null)
            projectionDTO = investmentService.getProjectionBasedOnCdiRate(userId,  Estimate.MONTHLY);
        else
            projectionDTO = investmentService.getProjectionBasedOnCdiWithPercentage(userId,  Estimate.MONTHLY, percentage);
        return RestResponse.ok(projectionDTO);
    }

    @GET
    @Path("/daily-cdi/{userId}")
    public RestResponse<InvestmentProjectionDTO> getDailyInvestmentProgress(@PathParam("userId") Long userId,
                                                                            @RestQuery("percentage") BigDecimal percentage) {
        if(percentage == null)
            projectionDTO = investmentService.getProjectionBasedOnCdiRate(userId,  Estimate.DAILY);
        else
            projectionDTO = investmentService.getProjectionBasedOnCdiWithPercentage(userId,  Estimate.DAILY, percentage);
        return RestResponse.ok(projectionDTO);
    }

}
