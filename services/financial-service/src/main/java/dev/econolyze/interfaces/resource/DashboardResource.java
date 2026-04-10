package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.PagedResponse;
import dev.econolyze.application.dto.response.BalanceResponse;
import dev.econolyze.application.dto.response.InvestmentProjectionResponse;
import dev.econolyze.application.dto.response.TransactionResponse;
import dev.econolyze.application.services.BalanceService;
import dev.econolyze.application.services.InvestmentService;
import dev.econolyze.application.services.TransactionService;
import dev.econolyze.domain.enums.Estimate;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/dashboard")
public class DashboardResource {
    @Inject
    BalanceService balanceService;
    @Inject
    InvestmentService investmentService;
    @Inject
    TransactionService transactionService;
    @GET
    @Path("/balance")
    public Uni<RestResponse<BalanceResponse>> getDashboard() {
        return balanceService.getBalanceByUserId().map(RestResponse::ok);
    }
    @GET
    @Path("/investment")
    public Uni<RestResponse<InvestmentProjectionResponse>> getMonthlyCdiDashboard() {
        return investmentService.getProjectionBasedOnCdiRate(Estimate.MONTHLY).map(RestResponse::ok);
    }

    @GET
    @Path("/transaction")
    @Blocking
    public Uni<RestResponse<PagedResponse<TransactionResponse>>> getGoalsDashboard(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("5") int pageSize,
            @QueryParam("sortBy") @DefaultValue("id") String sortBy,
            @QueryParam("sortDir") @DefaultValue("DESC") String sortDir
    ){
        return transactionService.getAllTransactionsByUserId(page, pageSize, sortBy, sortDir, null, null)
                .map(RestResponse::ok);
    }
}
