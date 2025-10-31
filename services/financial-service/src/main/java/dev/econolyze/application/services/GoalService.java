package dev.econolyze.application.services;

import dev.econolyze.application.dto.FinancialGoalDTO;
import dev.econolyze.application.dto.GoalProgressDTO;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.mapper.FinancialGoalMapper;
import dev.econolyze.domain.entity.FinancialGoal;
import dev.econolyze.domain.enums.TransactionType;
import dev.econolyze.infrastructure.repository.FinancialGoalRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class GoalService {
    @Inject
    FinancialGoalRepository financialGoalRepository;
    @Inject
    FinancialGoalMapper financialGoalMapper;
    @Inject
    AnalyticsService analyticsService;
    @Inject
    TransactionService transactionService;

    @Transactional
    public FinancialGoalDTO createNewGoal(FinancialGoalDTO goalDTO) {
        FinancialGoal goal = financialGoalMapper.mapToEntity(goalDTO);
        financialGoalRepository.persist(goal);
        return financialGoalMapper.mapToDTO(goal);
    }

    public FinancialGoalDTO getGoalById(Long id){
        return financialGoalMapper.mapToDTO(financialGoalRepository.findById(id));
    }

    public GoalProgressDTO getGoalProgress(Long id, Long userId){
        FinancialGoalDTO financialGoalDTO = getGoalById(id);
        List<TransactionDTO> transactionDTO = transactionService.getTransactionByUserIdAndType(userId, TransactionType.INCOME);
        BigDecimal incomesSum = transactionDTO.stream().map(TransactionDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return analyticsService.analyzeGoalProgress(financialGoalDTO, incomesSum);
    }

}
