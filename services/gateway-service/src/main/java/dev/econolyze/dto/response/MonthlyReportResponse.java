package dev.econolyze.dto.response;

public record MonthlyReportResponse(
    Long id,
    String month,
    Double income,
    Double expenses,
    Double balance
) {}
