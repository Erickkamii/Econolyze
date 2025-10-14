package dev.econolyze.dto;

public record MonthlyReportResponse(
    Long id,
    String month,
    Double income,
    Double expenses,
    Double balance
) {}
