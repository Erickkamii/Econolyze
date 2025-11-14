package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {
    public Optional<Account> findByAccountIdAndUserId(Long accountId, Long userId) {
        return find("id = ?1 and userId = ?2", accountId, userId).firstResultOptional();
    }

    public List<Account> listAllByUserId(Long userId) {
        return list("userId", userId);
    }
}
