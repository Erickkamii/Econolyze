package dev.econolyze.application.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.application.dto.FinancialGoalDTO;
import dev.econolyze.application.dto.GoalProgressDTO;
import dev.econolyze.application.dto.IncomeDTO;
import dev.econolyze.domain.entity.FinancialGoal;
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
    ObjectMapper objectMapper;
    @Inject
    AnalyticsService analyticsService;
    @Inject
    BalanceService balanceService;
    @Inject
    IncomeService incomeService;

    @Transactional
    public FinancialGoalDTO createNewGoal(FinancialGoalDTO goalDTO) {
        FinancialGoal goal = objectMapper.convertValue(goalDTO, FinancialGoal.class);
        financialGoalRepository.persist(goal);
        return mapToDTO(goal);
    }

    public FinancialGoalDTO getGoalById(Long id){
        return mapToDTO(financialGoalRepository.findById(id));
    }

    public GoalProgressDTO getGoalProgress(Long id, Long userId){
        FinancialGoalDTO financialGoalDTO = getGoalById(id);
        List<IncomeDTO> incomesDto = incomeService.getIncomesByUserId(userId);
        BigDecimal incomesSum = incomesDto.stream().map(IncomeDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return analyticsService.analyzeGoalProgress(financialGoalDTO, incomesSum);
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

    public void savingOrInvestment(FinancialGoalDTO financialGoalDTO, BalanceDTO balanceDTO){
        if(financialGoalDTO.getType().equals("SAVING")||financialGoalDTO.getType().equals("INVESTMENT")){
            if(financialGoalDTO.getStatus().equals("ACTIVE"))
                balanceDTO.setBalance(balanceDTO.getBalance().subtract(financialGoalDTO.getAmount()));

        } else {
            balanceDTO.setBalance(balanceDTO.getBalance().add(financialGoalDTO.getAmount()));
        }
        balanceService.setBalanceDifference(balanceDTO);
    }
}
