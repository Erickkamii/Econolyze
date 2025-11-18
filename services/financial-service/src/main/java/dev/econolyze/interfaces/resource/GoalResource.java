package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.GoalProgressDTO;
import dev.econolyze.application.dto.request.FinancialGoalRequest;
import dev.econolyze.application.dto.response.FinancialGoalResponse;
import dev.econolyze.application.dto.response.InvestmentProjectionResponse;
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

    InvestmentProjectionResponse projectionDTO;

    @POST
    public RestResponse<FinancialGoalResponse> createNewGoal(FinancialGoalRequest request) {
        FinancialGoalResponse createdGoal = goalService.createNewGoal(request);
        return RestResponse.ok(createdGoal);
    }

    @GET
    @Path("/all")
    public String getAll() {
        return "all goals";
    }

    @GET
    @Path("/{goalId}")
    public RestResponse<FinancialGoalResponse> getGoalById(@PathParam("goalId") Long goalId) {
        FinancialGoalResponse goalDTO = goalService.getGoalById(goalId);
        return RestResponse.ok(goalDTO);
    }

    @GET
    @Path("/progress/{goalId}")
    public RestResponse<GoalProgressDTO> getGoalProgress(@PathParam("goalId") Long goalId) {
        GoalProgressDTO progressDTO = goalService.getGoalProgress(goalId);
        return RestResponse.ok(progressDTO);
    }

    @GET
    @Path("/annual-cdi")
    public RestResponse<InvestmentProjectionResponse> getAnnualInvestmentProgress(@RestQuery("percentage") BigDecimal percentage){
        if ( percentage == null)
            projectionDTO = investmentService.getProjectionBasedOnCdiRate(Estimate.YEARLY);
        else
            projectionDTO = investmentService.getProjectionBasedOnCdiWithPercentage(Estimate.YEARLY, percentage);
        return RestResponse.ok(projectionDTO);
    }

    @GET
    @Path("/monthly-cdi")
    public RestResponse<InvestmentProjectionResponse> getMonthlyInvestmentProgress(
                                                                              @RestQuery("percentage") BigDecimal percentage) {
        if ( percentage == null)
            projectionDTO = investmentService.getProjectionBasedOnCdiRate(Estimate.MONTHLY);
        else
            projectionDTO = investmentService.getProjectionBasedOnCdiWithPercentage(Estimate.MONTHLY, percentage);
        return RestResponse.ok(projectionDTO);
    }

    @GET
    @Path("/daily-cdi")
    public RestResponse<InvestmentProjectionResponse> getDailyInvestmentProgress(@RestQuery("percentage") BigDecimal percentage) {
        if(percentage == null)
            projectionDTO = investmentService.getProjectionBasedOnCdiRate(Estimate.DAILY);
        else
            projectionDTO = investmentService.getProjectionBasedOnCdiWithPercentage(Estimate.DAILY, percentage);
        return RestResponse.ok(projectionDTO);
    }

}
