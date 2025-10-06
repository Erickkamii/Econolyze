package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.application.dto.GoalProgressDTO;
import dev.econolyze.application.dto.InvestmentProjectionDTO;
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
    @Path("/balance/{userId}")
    public RestResponse<BalanceDTO> getDashboard(@PathParam("userId") Long userId) {
        BalanceDTO balanceDTO = balanceService.getBalanceByUserId(userId);
        return RestResponse.ok(balanceDTO);
    }
    @GET
    @Path("/investment/{userId}")
    public RestResponse<InvestmentProjectionDTO> getMonthlyCdiDashboard(@PathParam("userId") Long userId) {
        InvestmentProjectionDTO projectionDTO = investmentService.getProjectionBasedOnCdiRate(userId, Estimate.MONTHLY);
        return RestResponse.ok(projectionDTO);
    }
    @GET
    @Path("/goal/{userId}")
    public RestResponse<List<GoalProgressDTO>> getGoalsDashboard(@PathParam("userId") Long userId){
        List<GoalProgressDTO> allGoals = analyticsService.analyzeAllGoalProgress(userId);
        return RestResponse.ok(allGoals);
    }
}
