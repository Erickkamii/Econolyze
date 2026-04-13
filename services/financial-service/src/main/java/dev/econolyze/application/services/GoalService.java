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
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

    @WithTransaction
    public Uni<FinancialGoalResponse> createNewGoal(FinancialGoalRequest request) {
        FinancialGoal goal = financialGoalMapper.mapToEntity(dtoBuilder(request));
        return financialGoalRepository.persist(goal).map(financialGoalMapper::mapToResponse);
    }

    @WithSession
    public Uni<FinancialGoalResponse> getGoalById(Long id){
        return financialGoalRepository.findById(id).map(financialGoalMapper::mapToResponse);
    }

    @WithSession
    public Uni<GoalProgressDTO> getGoalProgress(Long id){
        Long userId = userContext.getUserId();
        return getGoalById(id)
                .flatMap(gr -> {
                    FinancialGoalDTO goal = financialGoalMapper.mapToDTO(gr);
                    return transactionService.getTransactionByUserIdAndType(userId, TransactionType.INCOME)
                            .map(transactions -> removeIfNotMatch(transactions, id))
                            .map(t -> {
                                BigDecimal incomesSum = t.stream()
                                        .map(TransactionDTO::getAmount)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                                return analyticsService.analyzeGoalProgress(goal, incomesSum);
                            });
                });
    }

    @WithSession
    public Uni<List<FinancialGoalResponse>> getAllGoals() {
        return financialGoalRepository.getAllGoalsByUserId(userContext.getUserId())
                .map(g -> g.stream()
                        .map(financialGoalMapper::mapToResponse)
                        .toList());
    }

    private List<TransactionDTO> removeIfNotMatch(List<TransactionDTO> transactionDTOS, Long goalId){
        return transactionDTOS.stream()
                .filter(t -> t.getFinancialGoalId() != null && t.getFinancialGoalId().equals(goalId))
                .toList();
    }

    private FinancialGoalDTO dtoBuilder(FinancialGoalRequest request) {
        return FinancialGoalDTO.builder()
                .name(request.name())
                .userId(userContext.getUserId())
                .amount(request.amount())
                .description(request.description())
                .type(request.type())
                .status(request.status())
                .build();
    }
}
