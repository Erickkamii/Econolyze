package dev.econolyze.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
    Long id,
    BigDecimal amount,
    Long userId,
    String category,
    String type,
    String description,
    LocalDate date,
    String method,
    Integer financialGoalId
) {}
