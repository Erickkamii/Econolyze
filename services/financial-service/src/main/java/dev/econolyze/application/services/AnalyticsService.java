package dev.econolyze.application.services;

import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.application.dto.FinancialGoalDTO;
import dev.econolyze.application.dto.GoalProgressDTO;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    public GoalProgressDTO analyzeGoalProgress(FinancialGoalDTO financialGoal, BigDecimal incomesSum) {
        GoalProgressDTO goalProgressDTO = new GoalProgressDTO();
        goalProgressDTO.setId(financialGoal.getId());
        goalProgressDTO.setName(financialGoal.getName());
        goalProgressDTO.setProgress(incomesSum
                .divide(financialGoal.getAmount(), RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).doubleValue());
        return goalProgressDTO;
    }
}
