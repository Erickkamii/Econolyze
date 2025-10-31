package dev.econolyze.application.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.mapper.BalanceMapper;
import dev.econolyze.domain.entity.Balance;
import dev.econolyze.infrastructure.repository.BalanceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    @Inject
    BalanceRepository balanceRepository;
    @Inject
    BalanceMapper balanceMapper;
    @Inject
    TransactionService transactionService;

    @Transactional
    public BalanceDTO getBalanceByUserId(Long userId){
        Balance balance = balanceRepository.findById(userId);
        return BalanceDTO.builder()
                .userId(balance.getUserId())
                .balance(balance.getBalance())
                .date(balance.getDate())
                .balanceDifference(setBalanceDifference(balanceMapper.mapToDTO(balance)))
                .income(balance.getIncome())
                .expenses(balance.getExpenses())
                .build();
    }

    public BigDecimal setBalanceDifference(BalanceDTO balanceDTO){
        return balanceDTO.getBalance().subtract(balanceDTO.getIncome().subtract(balanceDTO.getExpenses()));


    }

    public void updateUserBalance(TransactionDTO transactionDTO, Long userId) {
        Balance balance = balanceRepository.findById(userId);
        if (balance != null) {
            transactionService.persistTransaction(transactionDTO, balance);
        } else {
            balance = transactionService.newBalance(userId);
            transactionService.persistTransaction(transactionDTO, balance);
        }
    }
}
