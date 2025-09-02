package dev.econolyze.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.econolyze.dto.BalanceDTO;
import dev.econolyze.entity.Balance;
import dev.econolyze.repository.BalanceRepository;
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
    ObjectMapper objectMapper;

    @Transactional
    public BalanceDTO getBalanceByUserId(Long userId){
        Balance balance = balanceRepository.findById(userId);
        return BalanceDTO.builder()
                .userId(balance.getUserId())
                .balance(balance.getBalance())
                .date(balance.getDate())
                .balanceDifference(setBalanceDifference(objectMapper.convertValue(balance, BalanceDTO.class)))
                .income(balance.getIncome())
                .expenses(balance.getExpenses())
                .build();
    }

    public BigDecimal setBalanceDifference(BalanceDTO balanceDTO){
        return balanceDTO.getBalance().subtract(balanceDTO.getIncome().subtract(balanceDTO.getExpenses()));
    }
}
