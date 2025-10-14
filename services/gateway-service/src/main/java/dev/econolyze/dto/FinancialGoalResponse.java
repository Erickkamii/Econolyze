package dev.econolyze.dto;

import java.math.BigDecimal;

public record FinancialGoalResponse(
    Long id,
    String name,
    BigDecimal amount,
    String description,
    String type,
    String status
) {}
