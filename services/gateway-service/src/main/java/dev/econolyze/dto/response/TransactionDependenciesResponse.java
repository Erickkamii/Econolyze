package dev.econolyze.dto.response;

import java.util.List;

public record TransactionDependenciesResponse(
        List<AccountResponse> accounts,
        List<FinancialGoalResponse> goals
) {
}
