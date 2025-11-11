package dev.econolyze.application.dto.request;

import dev.econolyze.domain.enums.GoalStatus;
import dev.econolyze.domain.enums.GoalType;

import java.math.BigDecimal;

public record FinancialGoalRequest(
        String name,
        BigDecimal amount,
        String description,
        GoalType type,
        GoalStatus status
) {
}
