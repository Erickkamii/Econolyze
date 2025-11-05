package dev.econolyze.application.services;

import dev.econolyze.application.dto.FinancialGoalDTO;
import dev.econolyze.application.dto.GoalProgressDTO;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.domain.entity.FinancialGoal;
import dev.econolyze.infrastructure.repository.FinancialGoalRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    @Inject
    FinancialGoalRepository financialGoalRepository;
    @Inject
    GoalService goalService;
    @Inject
    UserContext userContext;

    public GoalProgressDTO analyzeGoalProgress(FinancialGoalDTO financialGoal, BigDecimal incomesSum) {
        GoalProgressDTO goalProgressDTO = new GoalProgressDTO();
        goalProgressDTO.setId(financialGoal.getId());
        goalProgressDTO.setName(financialGoal.getName());
        goalProgressDTO.setProgress(incomesSum
                .divide(financialGoal.getAmount(), RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).doubleValue());
        return goalProgressDTO;
    }

    public List<GoalProgressDTO> analyzeAllGoalProgress(){
        Long userId = userContext.getUserId();
        List<FinancialGoal> allGoals = Optional.ofNullable(financialGoalRepository.getAllGoalsByUserId(userId))
                .orElse(Collections.emptyList());
        return allGoals.stream()
                .filter(Objects::nonNull)
                .filter(goal -> goal.getId() != null)
                .map(goal ->Optional.ofNullable(goalService.getGoalProgress(goal.getId()))
                        .orElse(new GoalProgressDTO())
        ).toList();
    }
}
