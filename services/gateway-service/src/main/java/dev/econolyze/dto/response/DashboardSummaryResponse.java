package dev.econolyze.dto.response;

public record DashboardSummaryResponse(
    Long id,
    Long income,
    Long expenses,
    Long balance
) {}
