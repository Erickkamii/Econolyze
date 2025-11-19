package dev.econolyze.resource;

import dev.econolyze.client.DashboardClient;
import dev.econolyze.dto.response.BalanceResponse;
import dev.econolyze.dto.response.DashboardSummaryResponse;
import dev.econolyze.dto.response.GoalProgressResponse;
import dev.econolyze.dto.response.InvestmentProjectionResponse;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api/dashboard")
@Authenticated
public class DashboardResource {
    @Inject
    @RestClient
    DashboardClient dashboardClient;
    @Inject
    JsonWebToken jwt;

    @GET
    public RestResponse<DashboardSummaryResponse> getDashboardFull(@Context HttpHeaders headers){
        String authorization = headers.getHeaderString("Authorization");
        if (jwt.getClaim("userId") == null){
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        try {
            RestResponse<BalanceResponse> balanceResponse = dashboardClient.getBalance(authorization);
            RestResponse<InvestmentProjectionResponse> investmentProjectionResponse = dashboardClient.getMonthlyCdiDashboard(authorization);
            RestResponse<List<GoalProgressResponse>> goalProgressResponse = dashboardClient.getGoalsDashboard(authorization);
            return RestResponse.ok(new DashboardSummaryResponse(
                    balanceResponse.getEntity(),
                    investmentProjectionResponse.getEntity(),
                    goalProgressResponse.getEntity()
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/balance")
    public RestResponse<BalanceResponse> getDashboard(@Context HttpHeaders headers) {
        String authorization = headers.getHeaderString("Authorization");
        if (jwt.getClaim("userId") == null) {
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        try {
            return dashboardClient.getBalance(authorization);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GET
    @Path("/investment")
    public RestResponse<InvestmentProjectionResponse> getMonthlyCdiDashboard(@Context HttpHeaders headers) {
        String authorization = headers.getHeaderString("Authorization");
        if (jwt.getClaim("userId") == null) {
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        try {
            return dashboardClient.getMonthlyCdiDashboard(authorization);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GET
    @Path("/goals")
    public RestResponse<List<GoalProgressResponse>> getGoalsDashboard(@Context HttpHeaders headers) {
        String authorization = headers.getHeaderString("Authorization");
        if (jwt.getClaim("userId") == null) {
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        try {
            return dashboardClient.getGoalsDashboard(authorization);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
