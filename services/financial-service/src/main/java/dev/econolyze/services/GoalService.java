package dev.econolyze.services;

import dev.econolyze.dto.FinancialGoalDTO;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class GoalService {

    public FinancialGoalDTO createNewGoal(FinancialGoalDTO goal) {
        log.info("Creating new goal: {}", goal);
        return goal;
    }
}
