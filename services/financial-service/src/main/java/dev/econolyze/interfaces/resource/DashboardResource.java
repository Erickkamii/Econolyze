package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.GoalProgressDTO;
import dev.econolyze.application.dto.InvestmentProjectionDTO;
import dev.econolyze.application.dto.response.BalanceResponse;
import dev.econolyze.application.services.AnalyticsService;
import dev.econolyze.application.services.BalanceService;
import dev.econolyze.application.services.InvestmentService;
import dev.econolyze.domain.enums.Estimate;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api/dashboard")
public class DashboardResource {
    @Inject
    BalanceService balanceService;
    @Inject
    InvestmentService investmentService;
    @Inject
    AnalyticsService analyticsService;
    @GET
    @Path("/balance")
    public RestResponse<BalanceResponse> getDashboard() {
        BalanceResponse balanceResponse = balanceService.getBalanceByUserId();
        return RestResponse.ok(balanceResponse);
    }
    @GET
    @Path("/investment")
    public RestResponse<InvestmentProjectionDTO> getMonthlyCdiDashboard() {
        InvestmentProjectionDTO projectionDTO = investmentService.getProjectionBasedOnCdiRate(Estimate.MONTHLY);
        return RestResponse.ok(projectionDTO);
    }
    @GET
    @Path("/goals")
    public RestResponse<List<GoalProgressDTO>> getGoalsDashboard(){
        List<GoalProgressDTO> allGoals = analyticsService.analyzeAllGoalProgress();
        return RestResponse.ok(allGoals);
    }
}
