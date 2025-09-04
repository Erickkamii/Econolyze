package dev.econolyze.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.econolyze.dto.FinancialGoalDTO;
import dev.econolyze.entity.FinancialGoal;
import dev.econolyze.repository.FinancialGoalRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class GoalService {
    @Inject
    FinancialGoalRepository financialGoalRepository;
    @Inject
    ObjectMapper objectMapper;

    @Transactional
    public FinancialGoalDTO createNewGoal(FinancialGoalDTO goalDTO) {
        FinancialGoal goal = objectMapper.convertValue(goalDTO, FinancialGoal.class);
        financialGoalRepository.persist(goal);
        return mapToDTO(goal);
    }

    private FinancialGoalDTO mapToDTO(FinancialGoal goal) {
        return FinancialGoalDTO.builder()
                .id(goal.getId())
                .userId(goal.getUserId())
                .name(goal.getName())
                .amount(goal.getAmount())
                .description(goal.getDescription())
                .type(goal.getType().toString())
                .status(goal.getStatus().toString()).build();
    }
}
