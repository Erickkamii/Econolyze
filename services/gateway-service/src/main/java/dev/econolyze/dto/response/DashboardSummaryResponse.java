package dev.econolyze.dto.response;


import java.util.List;

public record DashboardSummaryResponse(
    BalanceResponse balance,
    InvestmentProjectionResponse investment,
    List<TransactionResponse> transactions
) {}
