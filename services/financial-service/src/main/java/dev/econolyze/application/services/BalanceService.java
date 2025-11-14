package dev.econolyze.application.services;

import dev.econolyze.application.dto.AccountDTO;
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
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    @Inject
    BalanceRepository balanceRepository;
    @Inject
    BalanceMapper balanceMapper;
    @Inject
    AccountService accountService;
    @Inject
    UserContext userContext;

    @Transactional
    public BalanceResponse getBalanceByUserId(){
        Long userId = userContext.getUserId();
        Balance balance = balanceRepository.findByUserIdForUpdate(userId);
        if(balance == null){
            balance = newBalance(userId);
            balanceRepository.persist(balance);
        } else {
            updateUserBalance(userId);
        }
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

    public void updateUserBalance(Long userId) {
        Balance balance = balanceRepository.findById(userId);

        if (balance == null) {
            throw new IllegalStateException("Balance not found for user: " + userId);
        }


        List<AccountDTO> accounts = accountService.getAllAccountsForBalance(userId);
        BigDecimal accountsSum = accounts.stream()
                .map(AccountDTO::getActualBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal newBalance =
                balance.getIncome()
                        .subtract(balance.getExpenses())
                        .add(accountsSum);
        balance.setBalance(newBalance);
        setBalanceDifference(balanceMapper.mapToDTO(balance));
        balanceRepository.persist(balance);
    }


    Balance newBalance(Long userId){
        return Balance.builder()
                .userId(userId)
                .balance(BigDecimal.ZERO)
                .date(LocalDate.now())
                .income(BigDecimal.ZERO)
                .expenses(BigDecimal.ZERO)
                .build();
    }
}
