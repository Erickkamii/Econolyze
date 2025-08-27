package dev.econolyze.services;

import dev.econolyze.dto.BalanceDTO;
import dev.econolyze.entity.Balance;
import dev.econolyze.repository.BalanceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    private static BalanceRepository balanceRepository;

    public static BalanceDTO getBalanceByUserId(Long userId){
        Balance balance = balanceRepository.findById(userId);
        return BalanceDTO.builder()
                .userId(balance.getUserId())
                .balance(balance.getBalance())
                .balanceDifference(balance.getBalanceDifference())
                .date(balance.getDate())
                .income(balance.getIncome())
                .expenses(balance.getExpenses())
                .build();
    }
}
