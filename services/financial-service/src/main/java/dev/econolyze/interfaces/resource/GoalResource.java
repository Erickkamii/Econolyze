package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.GoalProgressDTO;
import dev.econolyze.application.dto.request.FinancialGoalRequest;
import dev.econolyze.application.dto.response.FinancialGoalResponse;
import dev.econolyze.application.dto.response.InvestmentProjectionResponse;
import dev.econolyze.application.services.GoalService;
import dev.econolyze.application.services.InvestmentService;
import dev.econolyze.domain.enums.Estimate;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;

import java.math.BigDecimal;
import java.util.List;

@Path( "/api/goal")
public class GoalResource {

    @Inject
    GoalService goalService;
    @Inject
    InvestmentService investmentService;


    @POST
    public Uni<RestResponse<FinancialGoalResponse>> createNewGoal(FinancialGoalRequest request) {
        return goalService.createNewGoal(request).map(RestResponse::ok);
    }

    @GET
    public Uni<RestResponse<List<FinancialGoalResponse>>> getAll() {
        return goalService.getAllGoals().map(RestResponse::ok);
    }

    @GET
    @Path("/{goalId}")
    public Uni<RestResponse<FinancialGoalResponse>> getGoalById(@PathParam("goalId") Long goalId) {
        return goalService.getGoalById(goalId).map(RestResponse::ok);
    }

    @GET
    @Path("/progress/{goalId}")
    public Uni<RestResponse<GoalProgressDTO>> getGoalProgress(@PathParam("goalId") Long goalId) {
        return goalService.getGoalProgress(goalId).map(RestResponse::ok);
    }

    @GET
    @Path("/annual-cdi")
    public Uni<RestResponse<InvestmentProjectionResponse>> getAnnualInvestmentProgress(@RestQuery("percentage") BigDecimal percentage){
        Uni<InvestmentProjectionResponse> result = percentage == null
                ? investmentService.getProjectionBasedOnCdiRate(Estimate.YEARLY)
                : investmentService.getProjectionBasedOnCdiWithPercentage(Estimate.YEARLY, percentage);
        return result.map(RestResponse::ok);
    }

    @GET
    @Path("/monthly-cdi")
    public Uni<RestResponse<InvestmentProjectionResponse>> getMonthlyInvestmentProgress(@RestQuery("percentage") BigDecimal percentage) {
        Uni<InvestmentProjectionResponse> result = percentage == null
                ? investmentService.getProjectionBasedOnCdiRate(Estimate.MONTHLY)
                : investmentService.getProjectionBasedOnCdiWithPercentage(Estimate.MONTHLY, percentage);
        return result.map(RestResponse::ok);
    }

    @GET
    @Path("/daily-cdi")
    public Uni<RestResponse<InvestmentProjectionResponse>> getDailyInvestmentProgress(@RestQuery("percentage") BigDecimal percentage) {
        Uni<InvestmentProjectionResponse> result = percentage == null
                ? investmentService.getProjectionBasedOnCdiRate(Estimate.DAILY)
                : investmentService.getProjectionBasedOnCdiWithPercentage(Estimate.DAILY, percentage);
        return result.map(RestResponse::ok);
    }

}
