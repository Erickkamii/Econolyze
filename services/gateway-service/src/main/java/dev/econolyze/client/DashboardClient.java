package dev.econolyze.client;

import dev.econolyze.dto.response.BalanceResponse;
import dev.econolyze.dto.response.GoalProgressResponse;
import dev.econolyze.dto.response.InvestmentProjectionResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api/dashboard")
@RegisterRestClient(configKey = "financial-service")
@RegisterClientHeaders(AuthHeaderFactory.class)
public interface DashboardClient {
    @GET
    @Path("/balance")
    RestResponse<BalanceResponse> getBalance(
            @HeaderParam("Authorization") String authorization
    );
    @GET
    @Path("/investment")
    RestResponse<InvestmentProjectionResponse> getMonthlyCdiDashboard(
            @HeaderParam("Authorization") String authorization
    );
    @GET
    @Path("/goals")
    RestResponse<List<GoalProgressResponse>> getGoalsDashboard(
            @HeaderParam("Authorization") String authorization
    );
}
