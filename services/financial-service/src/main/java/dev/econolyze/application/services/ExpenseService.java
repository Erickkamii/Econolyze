package dev.econolyze.application.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.application.dto.ExpenseDTO;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.domain.entity.Balance;
import dev.econolyze.domain.entity.Expense;
import dev.econolyze.infrastructure.repository.ExpenseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class ExpenseService {
    @Inject
    BalanceService balanceService;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    ExpenseRepository expenseRepository;

    public List<ExpenseDTO> getAllExpensesByUserId(Long UserId){
        return expenseRepository.findAllExpensesByUserId(UserId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    protected void persistExpense(TransactionDTO transactionDTO, Balance balance) {
        balance.setExpenses(balance.getExpenses().add(transactionDTO.getAmount()));
        balance.setBalance(balance.getBalance().subtract(transactionDTO.getAmount()));
        balanceService.setBalanceDifference(objectMapper.convertValue(balance, BalanceDTO.class));
        ExpenseDTO eDTO = ExpenseDTO.builder()
                .amount(transactionDTO.getAmount())
                .category(transactionDTO.getCategory())
                .date(LocalDate.now())
                .userId(transactionDTO.getUserId())
                .description(transactionDTO.getDescription())
                .build();
        expenseRepository.persist(objectMapper.convertValue(eDTO, Expense.class));
    }

    private ExpenseDTO mapToDTO(Expense expense){
        return ExpenseDTO.builder()
                .id(expense.getId())
                .userId(expense.getUserId())
                .name(expense.getName())
                .date(expense.getDate())
                .category(expense.getCategory().toString())
                .financialGoalId(expense.getFinancialGoalId())
                .amount(expense.getAmount())
                .method(expense.getPaymentMethod().toString())
                .description(expense.getDescription())
                .build();
    }
}
