package dev.econolyze.client;

import dev.econolyze.dto.response.BalanceResponse;
import dev.econolyze.dto.response.InvestmentProjectionResponse;
import dev.econolyze.dto.response.PagedResponse;
import dev.econolyze.dto.response.TransactionResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/dashboard")
@RegisterRestClient(configKey = "financial-service")
@RegisterClientHeaders(AuthHeaderFactory.class)
public interface DashboardClient {
    @GET
    @Path("/balance")
    Uni<RestResponse<BalanceResponse>> getBalance(
            @HeaderParam("Authorization") String authorization
    );
    @GET
    @Path("/investment")
    Uni<RestResponse<InvestmentProjectionResponse>> getMonthlyCdiDashboard(
            @HeaderParam("Authorization") String authorization
    );
    @GET
    @Path("/transaction")
    Uni<RestResponse<PagedResponse<TransactionResponse>>> getTransactions(
            @HeaderParam("Authorization") String authorization,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("10") int pageSize
    );
}
