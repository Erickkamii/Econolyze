package dev.econolyze.application.services;

import dev.econolyze.application.dto.FinancialGoalDTO;
import dev.econolyze.application.dto.GoalProgressDTO;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.dto.request.FinancialGoalRequest;
import dev.econolyze.application.dto.response.FinancialGoalResponse;
import dev.econolyze.application.mapper.FinancialGoalMapper;
import dev.econolyze.application.security.UserContext;
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
    @Inject
    UserContext userContext;

    @Transactional
    public FinancialGoalResponse createNewGoal(FinancialGoalRequest request) {
        FinancialGoal goal = financialGoalMapper.mapToEntity(dtoBuilder(request));
        financialGoalRepository.persist(goal);
        return financialGoalMapper.mapToResponse(goal);
    }

    public FinancialGoalResponse getGoalById(Long id){
        return financialGoalMapper.mapToResponse(financialGoalRepository.findById(id));
    }

    public GoalProgressDTO getGoalProgress(Long id){
        Long userId = userContext.getUserId();
        FinancialGoalDTO financialGoalDTO = financialGoalMapper.mapToDTO(getGoalById(id));
        List<TransactionDTO> transactionDTO = removeIfNotMatch(transactionService.getTransactionByUserIdAndType(userId, TransactionType.INCOME), financialGoalDTO.getId());
        BigDecimal incomesSum = transactionDTO.stream().map(TransactionDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return analyticsService.analyzeGoalProgress(financialGoalDTO, incomesSum);
    }

    private List<TransactionDTO> removeIfNotMatch(List<TransactionDTO> transactionDTOS, Long goalId){
        return transactionDTOS.stream()
                .filter(t -> t.getFinancialGoalId().equals(goalId))
                .toList();
    }

    private FinancialGoalDTO dtoBuilder(FinancialGoalRequest request) {
        Long userId = userContext.getUserId();
        return FinancialGoalDTO.builder()
                .name(request.name())
                .userId(userId)
                .amount(request.amount())
                .description(request.description())
                .type(request.type())
                .status(request.status())
                .build();
    }

}
