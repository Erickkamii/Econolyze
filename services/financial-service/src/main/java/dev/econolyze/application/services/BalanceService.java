package dev.econolyze.application.services;

import dev.econolyze.application.dto.AccountDTO;
import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.application.dto.response.BalanceResponse;
import dev.econolyze.application.mapper.BalanceMapper;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.domain.entity.Balance;
import dev.econolyze.infrastructure.repository.BalanceRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @WithTransaction
    public Uni<BalanceResponse> getBalanceByUserId(){
        Long userId = userContext.getUserId();
        return balanceRepository.findByUserIdForUpdate(userId)
                .flatMap(balance -> {
                    if (balance == null){
                        Balance newBalance = newBalance(userId);
                        return balanceRepository.persist(newBalance)
                                .map(balanceMapper::mapToDTO)
                                .map(dto -> new BalanceResponse(
                                        newBalance.getBalance(),
                                        newBalance.getDate(),
                                        newBalance.getIncome(),
                                        newBalance.getExpenses(),
                                        setBalanceDifference(dto)
                                ));
                    } else{
                        return updateUserBalance(userId)
                                .map(ignored -> new BalanceResponse(
                                        balance.getBalance(),
                                        balance.getDate(),
                                        balance.getIncome(),
                                        balance.getExpenses(),
                                        setBalanceDifference(balanceMapper.mapToDTO(balance))
                                ));
                    }
                });
    }

    public BigDecimal setBalanceDifference(BalanceDTO balanceDTO){
        return balanceDTO.getBalance().subtract(balanceDTO.getIncome().subtract(balanceDTO.getExpenses()));
    }

    @WithSession
    public Uni<Void> updateUserBalance(Long userId) {
        return balanceRepository.findById(userId)
                .onItem().ifNull().failWith(() -> new NotFoundException("Balance not found!"))
                .flatMap(balance ->
                        accountService.getAllAccountsForBalance(userId)
                                .map(accounts -> {
                                    BigDecimal accountsSum = accounts.stream()
                                            .map(AccountDTO::getActualBalance)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                                    balance.setBalance(balance.getIncome()
                                            .subtract(balance.getExpenses())
                                            .add(accountsSum));
                                    return balance;
                                })
                .flatMap(balanceRepository::persist)
                                .replaceWithVoid());
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
