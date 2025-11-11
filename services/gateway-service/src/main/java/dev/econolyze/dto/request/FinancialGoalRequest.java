package dev.econolyze.dto.request;

import java.math.BigDecimal;

public record FinancialGoalRequest(
        Long id,
        String name,
        BigDecimal amount,
        String description,
        String type,
        String status
) {
}
