package dev.econolyze.resource;

import dev.econolyze.aggregator.DashboardAggregator;
import dev.econolyze.client.DashboardClient;
import dev.econolyze.dto.response.BalanceResponse;
import dev.econolyze.dto.response.DashboardSummaryResponse;
import dev.econolyze.dto.response.InvestmentProjectionResponse;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/dashboard")
@Authenticated
public class DashboardResource {
    @Inject
    @RestClient
    DashboardClient dashboardClient;
    @Inject
    DashboardAggregator dashboardAggregator;
    @Inject
    JsonWebToken jwt;

    private boolean unauthorized(){
        return jwt.getClaim("userId") == null;
    }

    @GET
    public Uni<RestResponse<DashboardSummaryResponse>> getDashboardFull(@Context HttpHeaders headers){
        if (unauthorized()) {
            return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        }
        return dashboardAggregator.load(headers.getHeaderString("Authorization"))
                .map(RestResponse::ok)
                .onFailure().invoke(Throwable::printStackTrace)
                .onFailure().recoverWithItem(RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR));
    }

    @GET
    @Path("/balance")
    public Uni<RestResponse<BalanceResponse>> getDashboard(@Context HttpHeaders headers) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return dashboardClient.getBalance(headers.getHeaderString("Authorization"))
                .onFailure().recoverWithItem(RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR));
    }
    @GET
    @Path("/investment")
    public Uni<RestResponse<InvestmentProjectionResponse>> getMonthlyCdiDashboard(@Context HttpHeaders headers) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return dashboardClient.getMonthlyCdiDashboard(headers.getHeaderString("Authorization"))
                .onFailure().recoverWithItem(RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR));
    }
}
