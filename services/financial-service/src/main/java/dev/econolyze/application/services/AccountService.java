package dev.econolyze.application.services;

import dev.econolyze.application.dto.AccountDTO;
import dev.econolyze.application.dto.request.CreateAccountRequest;
import dev.econolyze.application.dto.request.UpdateAccountRequest;
import dev.econolyze.application.dto.response.AccountResponse;
import dev.econolyze.application.mapper.AccountMapper;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.domain.entity.Account;
import dev.econolyze.domain.entity.Payment;
import dev.econolyze.infrastructure.repository.AccountRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    @Inject
    AccountRepository accountRepository;
    @Inject
    UserContext userContext;
    @Inject
    AccountMapper accountMapper;

    @WithTransaction
    public Uni<AccountResponse> createAccount(CreateAccountRequest request) {
        Account account = accountMapper.mapToEntity(request);
        account.setUserId(userContext.getUserId());
        account.setActive(true);
        return accountRepository.persist(account)
                .map(accountMapper::mapToResponse);
    }

    @WithTransaction
    public Uni<AccountResponse> updateAccount(UpdateAccountRequest request, Long accountId) {
        return accountRepository.findByAccountIdAndUserId(accountId, userContext.getUserId())
                .map(opt -> opt.orElseThrow(()-> new NotFoundException("Account not found or does not belong to the user.")))
                .map(account -> {
                    if(request.name() !=null) account.setName(request.name());
                    if(request.type() !=null) account.setType(request.type());
                    if(request.actualBalance() !=null) account.setActualBalance(request.actualBalance());
                    if(request.creditLimit() !=null) account.setCreditLimit(request.creditLimit());
                    if(request.closingDate() !=null) account.setClosingDate(request.closingDate());
                    if(request.active() !=null) account.setActive(request.active());
                    return account;
                })
                .flatMap(account -> accountRepository.persist(account)
                        .map(accountMapper::mapToResponse));
    }

    @WithTransaction
    public Uni<Void> deleteAccount(Long accountId) {
        return accountRepository.findByAccountIdAndUserId(accountId, userContext.getUserId())
                .map(opt -> opt.orElseThrow(()-> new NotFoundException("Account not found or does not belong to the user.")))
                .flatMap(account -> accountRepository.delete(account));
    }

    @WithSession
    public Uni<AccountResponse> getAccountById(Long accountId) {
        return accountRepository.findByAccountIdAndUserId(accountId, userContext.getUserId())
                .map(opt -> opt.orElseThrow(()-> new NotFoundException("Account not found or does not belong to the user.")))
                .map(accountMapper::mapToResponse);
    }

    @WithSession
    public Uni<List<AccountResponse>> getAllAccounts() {
        return accountRepository.listAllByUserId(userContext.getUserId())
                .map(accounts -> accounts.stream()
                        .map(accountMapper::mapToResponse)
                        .collect(Collectors.toList()));
    }

    Uni<Void> updateAccountBalance(Payment payment){
        return accountRepository.findById(payment.getAccountId())
                .onItem().ifNull().failWith(() -> new NotFoundException("Account not found!"))
                .map(account -> {
                    BigDecimal amount = payment.getAmount();
                    if (payment.getTransaction().getType().isIncreaseBalance()){
                        account.setActualBalance(account.getActualBalance().add(amount));
                    } else if (payment.getTransaction().getType().isDecreaseBalance()){
                        account.setActualBalance(account.getActualBalance().subtract(amount));
                    }
                    return account;
                })
                .flatMap(accountRepository::persist)
                .replaceWithVoid();
    }

    Uni<List<AccountDTO>> getAllAccountsForBalance(Long userId){
        return accountRepository.listAllByUserId(userId).map(accounts -> accounts.stream().map(accountMapper::mapToDTO).collect(Collectors.toList()));
    }
}
