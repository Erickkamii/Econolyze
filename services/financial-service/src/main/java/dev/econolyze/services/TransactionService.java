package dev.econolyze.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.econolyze.dto.BalanceDTO;
import dev.econolyze.dto.ExpenseDTO;
import dev.econolyze.dto.IncomeDTO;
import dev.econolyze.dto.TransactionDTO;
import dev.econolyze.entity.Expense;
import dev.econolyze.entity.Income;
import dev.econolyze.entity.Transaction;
import dev.econolyze.repository.BalanceRepository;
import dev.econolyze.repository.ExpenseRepository;
import dev.econolyze.repository.IncomeRepository;
import dev.econolyze.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    IncomeRepository incomeRepository;
    @Inject
    ExpenseRepository expenseRepository;

    public List<TransactionDTO> getAllTransactionsByUserId(Long userId) {
        return transactionRepository.findAllTransactionsByUserId(userId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = objectMapper.convertValue(transactionDTO, Transaction.class);
        transactionRepository.persist(transaction);
        updateUserBalance(transactionDTO, transaction.getUserId());
        return mapToDTO(transaction);
    }

    private void updateUserBalance(TransactionDTO transactionDTO, Long userId) {
        var balance = balanceRepository.findById(userId);
        if (balance != null) {
            if(transactionDTO.getType().equals("INCOME")||transactionDTO.getType().equals("REFUND")){
                balance.setIncome(transactionDTO.getAmount());
                balance.setBalance(balance.getBalance().add(transactionDTO.getAmount()));
                IncomeDTO iDto = IncomeDTO.builder()
                        .amount(transactionDTO.getAmount())
                        .category(transactionDTO.getCategory())
                        .date(LocalDate.now())
                        .name(transactionDTO.getDescription())
                        .build();
                incomeRepository.persist(objectMapper.convertValue(iDto, Income.class));
            } else {
                balance.setExpenses(transactionDTO.getAmount());
                balance.setBalance(balance.getBalance().subtract(transactionDTO.getAmount()));
                ExpenseDTO eDTO = ExpenseDTO.builder()
                        .amount(transactionDTO.getAmount())
                        .category(transactionDTO.getCategory())
                        .date(LocalDate.now())
                        .name(transactionDTO.getDescription())
                        .build();
                expenseRepository.persist(objectMapper.convertValue(eDTO, Expense.class));
            }
            balanceRepository.persist(balance);
        }
        objectMapper.convertValue(balance, BalanceDTO.class);
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
}
