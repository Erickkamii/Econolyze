package dev.econolyze.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IncomeResponse(
    Long id,
    String name,
    Long userId,
    BigDecimal amount,
    String category,
    String description,
    LocalDate date,
    String method,
    Integer financialGoalId
) {}
