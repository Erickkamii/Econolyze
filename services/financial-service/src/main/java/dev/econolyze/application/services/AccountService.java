package dev.econolyze.application.services;

import dev.econolyze.application.dto.AccountDTO;
import dev.econolyze.application.dto.PaymentDTO;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.dto.request.CreateAccountRequest;
import dev.econolyze.application.dto.request.UpdateAccountRequest;
import dev.econolyze.application.dto.response.AccountResponse;
import dev.econolyze.application.mapper.AccountMapper;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.domain.entity.Account;
import dev.econolyze.domain.entity.Payment;
import dev.econolyze.infrastructure.repository.AccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        Account account = accountMapper.mapToEntity(request);
        account.setUserId(userContext.getUserId());
        account.setActive(true);
        accountRepository.persist(account);
        return accountMapper.mapToResponse(account);
    }

    @Transactional
    public AccountResponse updateAccount(UpdateAccountRequest request, Long accountId) {
        Account account = accountRepository.findByAccountIdAndUserId(accountId, userContext.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found or does not belong to the user."));
        if (request.name() != null) {
            account.setName(request.name());
        }
        if (request.type() != null) {
            account.setType(request.type());
        }
        if (request.actualBalance() != null) {
            account.setActualBalance(request.actualBalance());
        }
        if (request.creditLimit() != null) {
            account.setCreditLimit(request.creditLimit());
        }
        if (request.closingDate() != null) {
            account.setClosingDate(request.closingDate());
        }
        if (request.active() != null) {
            account.setActive(request.active());
        }
        accountRepository.persist(account);
        return accountMapper.mapToResponse(account);
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findByAccountIdAndUserId(accountId, userContext.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found or does not belong to the user."));
        accountRepository.delete(account);
    }

    public AccountResponse getAccountById(Long accountId) {
        Long userId = userContext.getUserId();
        Account account = accountRepository.findByAccountIdAndUserId(accountId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found or does not belong to the user."));
        return accountMapper.mapToResponse(account);
    }

    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.listAllByUserId(userContext.getUserId());
        return accounts.stream()
                .map(accountMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    void updateAccountBalance(Payment payment){
        Account account = accountRepository.findById(payment.getAccountId());
        if(account == null)
            throw new RuntimeException("Account not found");
        BigDecimal amount = payment.getAmount();

        if (payment.getTransaction().getType().isIncreaseBalance()) {
            account.setActualBalance(account.getActualBalance().add(amount));
        } else if (payment.getTransaction().getType().isDecreaseBalance()) {
            account.setActualBalance(account.getActualBalance().subtract(amount));
        }

        accountRepository.persist(account);
    }

    List<AccountDTO> getAllAccountsForBalance(Long userId){
        return accountRepository.listAllByUserId(userId).stream()
                .map(accountMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}
