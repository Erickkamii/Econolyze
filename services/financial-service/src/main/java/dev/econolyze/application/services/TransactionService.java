package dev.econolyze.application.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.application.dto.ExpenseDTO;
import dev.econolyze.application.dto.IncomeDTO;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.domain.entity.Balance;
import dev.econolyze.domain.entity.Expense;
import dev.econolyze.domain.entity.Income;
import dev.econolyze.domain.entity.Transaction;
import dev.econolyze.domain.enums.TransactionType;
import dev.econolyze.infrastructure.repository.BalanceRepository;
import dev.econolyze.infrastructure.repository.ExpenseRepository;
import dev.econolyze.infrastructure.repository.IncomeRepository;
import dev.econolyze.infrastructure.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    @Inject
    TransactionRepository transactionRepository;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    BalanceRepository balanceRepository;
    @Inject
    BalanceService balanceService;
    @Inject
    IncomeService incomeService;
    @Inject
    ExpenseService expenseService;

    public List<TransactionDTO> getAllTransactionsByUserId(Long userId) {
        return transactionRepository.findAllTransactionsByUserId(userId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional
    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = objectMapper.convertValue(transactionDTO, Transaction.class);
        transactionRepository.persist(transaction);
        balanceService.updateUserBalance(transactionDTO, transaction.getUserId());
        return mapToDTO(transaction);
    }

    public TransactionDTO mapToDTO(Transaction t) {
        return TransactionDTO.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .type(t.getType().toString())
                .category(t.getCategory().toString())
                .date(t.getDate())
                .description(t.getDescription())
                .userId(t.getUserId())
                .method(t.getMethod().toString())
                .build();
    }

    protected BalanceDTO persistTransaction(TransactionDTO transactionDTO, Balance balance){
        TransactionType type = TransactionType.valueOf(transactionDTO.getType());
        if(type.isIncreaseBalance()){
            incomeService.persistIncome(transactionDTO, balance);
        } else if(type.isDecreaseBalance()){
            expenseService.persistExpense(transactionDTO, balance);
        }
        balanceRepository.persist(balance);
        return objectMapper.convertValue(balance, BalanceDTO.class);
    }

    protected Balance newBalance(Long userId){
        return Balance.builder()
                .userId(userId)
                .balance(BigDecimal.ZERO)
                .date(LocalDate.now())
                .income(BigDecimal.ZERO)
                .expenses(BigDecimal.ZERO)
                .build();
    }
}
