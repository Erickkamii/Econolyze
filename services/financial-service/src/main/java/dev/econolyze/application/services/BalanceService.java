package dev.econolyze.application.services;

import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.dto.response.BalanceResponse;
import dev.econolyze.application.mapper.BalanceMapper;
import dev.econolyze.application.security.UserContext;
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
    @Inject
    UserContext userContext;

    @Transactional
    public BalanceResponse getBalanceByUserId(){
        Long userId = userContext.getUserId();
        Balance balance = balanceRepository.findByUserIdForUpdate(userId);
        return new BalanceResponse(
                balance.getBalance(),
                balance.getDate(),
                balance.getIncome(),
                balance.getExpenses(),
                setBalanceDifference(balanceMapper.mapToDTO(balance)));
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
