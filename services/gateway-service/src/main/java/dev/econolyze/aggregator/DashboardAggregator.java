package dev.econolyze.aggregator;

import dev.econolyze.client.DashboardClient;
import dev.econolyze.dto.response.*;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;


@ApplicationScoped
public class DashboardAggregator {
    @Inject
    @RestClient
    DashboardClient dashboardClient;

    public Uni<DashboardSummaryResponse> load(String authorization){
        Uni<RestResponse<BalanceResponse>> balanceUni = dashboardClient.getBalance(authorization);
        Uni<RestResponse<InvestmentProjectionResponse>> investmentUni = dashboardClient.getMonthlyCdiDashboard(authorization);
        Uni<RestResponse<PagedResponse<TransactionResponse>>> transactionUni = dashboardClient.getTransactions(authorization,0, 10);

        return Uni.combine().all().unis(balanceUni, investmentUni, transactionUni).asTuple()
                .map(tuple -> new DashboardSummaryResponse(
                        tuple.getItem1().getEntity(),
                        tuple.getItem2().getEntity(),
                        tuple.getItem3().getEntity().content()
                ));
    }
}
