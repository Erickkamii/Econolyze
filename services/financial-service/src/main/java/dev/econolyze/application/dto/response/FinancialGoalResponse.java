package dev.econolyze.application.dto.response;

import dev.econolyze.domain.enums.GoalStatus;
import dev.econolyze.domain.enums.GoalType;

import java.math.BigDecimal;

public record FinancialGoalResponse(
        Long id,
        String name,
        BigDecimal amount,
        String description,
        GoalType type,
        GoalStatus status
) {
}
