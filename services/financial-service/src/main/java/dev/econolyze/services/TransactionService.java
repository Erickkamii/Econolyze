package dev.econolyze.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.econolyze.dto.BalanceDTO;
import dev.econolyze.dto.ExpenseDTO;
import dev.econolyze.dto.IncomeDTO;
import dev.econolyze.dto.TransactionDTO;
import dev.econolyze.entity.Balance;
import dev.econolyze.entity.Expense;
import dev.econolyze.entity.Income;
import dev.econolyze.entity.Transaction;
import dev.econolyze.repository.BalanceRepository;
import dev.econolyze.repository.ExpenseRepository;
import dev.econolyze.repository.IncomeRepository;
import dev.econolyze.repository.TransactionRepository;
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
    IncomeRepository incomeRepository;
    @Inject
    ExpenseRepository expenseRepository;

    public List<TransactionDTO> getAllTransactionsByUserId(Long userId) {
        return transactionRepository.findAllTransactionsByUserId(userId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional
    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = objectMapper.convertValue(transactionDTO, Transaction.class);
        transactionRepository.persist(transaction);
        updateUserBalance(transactionDTO, transaction.getUserId());
        return mapToDTO(transaction);
    }

    private void updateUserBalance(TransactionDTO transactionDTO, Long userId) {
        Balance balance = balanceRepository.findById(userId);
        BalanceDTO balanceDTO;
        if (balance != null) {
            balanceDTO = persistTransaction(transactionDTO, balance);
        } else {
            balance = newBalance(userId);
            balanceDTO = persistTransaction(transactionDTO, balance);
        }
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

    private BalanceDTO persistTransaction(TransactionDTO transactionDTO, Balance balance){
        if(transactionDTO.getType().equals("INCOME")||transactionDTO.getType().equals("REFUND")){
            balance.setIncome(balance.getIncome().add(transactionDTO.getAmount()));
            balance.setBalance(balance.getBalance().add(transactionDTO.getAmount()));
            IncomeDTO iDto = IncomeDTO.builder()
                    .amount(transactionDTO.getAmount())
                    .category(transactionDTO.getCategory())
                    .date(LocalDate.now())
                    .name(transactionDTO.getDescription())
                    .build();
            incomeRepository.persist(objectMapper.convertValue(iDto, Income.class));
        } else {
            balance.setExpenses(balance.getExpenses().add(transactionDTO.getAmount()));
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
        return objectMapper.convertValue(balance, BalanceDTO.class);
    }

    private Balance newBalance(Long userId){
        BalanceDTO balanceDTO =BalanceDTO.builder()
                .userId(userId)
                .balance(BigDecimal.ZERO)
                .balanceDifference(BigDecimal.ZERO)
                .date(LocalDate.now())
                .income(BigDecimal.ZERO)
                .expenses(BigDecimal.ZERO)
                .build();
        return objectMapper.convertValue(balanceDTO, Balance.class);
    }
}
