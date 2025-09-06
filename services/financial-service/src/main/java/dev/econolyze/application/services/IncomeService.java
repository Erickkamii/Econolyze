package dev.econolyze.application.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.application.dto.IncomeDTO;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.domain.entity.Balance;
import dev.econolyze.domain.entity.Income;
import dev.econolyze.infrastructure.repository.IncomeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class IncomeService {

    @Inject
    IncomeRepository incomeRepository;
    @Inject
    BalanceService balanceService;
    @Inject
    ObjectMapper objectMapper;

    public List<IncomeDTO> getIncomesByUserId(Long userId){
        return incomeRepository.findAllIncomesByUserId(userId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    private IncomeDTO mapToDTO(Income income){
        return IncomeDTO.builder()
                .id(income.getId())
                .userId(income.getUserId())
                .name(income.getName())
                .date(income.getDate())
                .category(income.getCategory().toString())
                .financialGoalId(income.getFinancialGoalId())
                .amount(income.getAmount())
                .description(income.getDescription())
                .build();
    }


    protected void persistIncome(TransactionDTO transactionDTO, Balance balance){
        balance.setIncome(balance.getIncome().add(transactionDTO.getAmount()));
        balance.setBalance(balance.getBalance().add(transactionDTO.getAmount()));
        balanceService.setBalanceDifference(objectMapper.convertValue(balance, BalanceDTO.class));
        IncomeDTO iDto = IncomeDTO.builder()
                .amount(transactionDTO.getAmount())
                .category(transactionDTO.getCategory())
                .date(LocalDate.now())
                .userId(transactionDTO.getUserId())
                .description(transactionDTO.getDescription())
                .build();
        incomeRepository.persist(objectMapper.convertValue(iDto, Income.class));
    }
}
