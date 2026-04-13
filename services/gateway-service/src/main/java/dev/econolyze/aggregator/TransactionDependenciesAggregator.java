package dev.econolyze.aggregator;

import dev.econolyze.client.AccountClient;
import dev.econolyze.client.GoalClient;
import dev.econolyze.dto.response.AccountResponse;
import dev.econolyze.dto.response.FinancialGoalResponse;
import dev.econolyze.dto.response.TransactionDependenciesResponse;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@ApplicationScoped
public class TransactionDependenciesAggregator {
    @Inject
    @RestClient
    AccountClient accountClient;
    @Inject
    @RestClient
    GoalClient financialGoalClient;

    public Uni<TransactionDependenciesResponse> load(String authorization){
        Uni<RestResponse<List<AccountResponse>>> accounts = accountClient.getAllAccounts(authorization);
        Uni<RestResponse<List<FinancialGoalResponse>>> goals = financialGoalClient.getAllGoals(authorization);
        return Uni.combine().all().unis(accounts, goals).asTuple()
                .map(t -> new TransactionDependenciesResponse(
                        t.getItem1().getEntity(),
                        t.getItem2().getEntity()
                ));
    }
}
