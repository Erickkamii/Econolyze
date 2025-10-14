package dev.econolyze.dto;

public record DashboardSummaryResponse(
    Long id,
    Long income,
    Long expenses,
    Long balance
) {}
