package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.Account;
import io.smallrye.mutiny.Uni;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {

    public Uni<Optional<Account>> findByAccountIdAndUserId(Long accountId, Long userId) {
        return find("id = ?1 and userId = ?2", accountId, userId)
                .firstResult()
                .map(Optional::ofNullable);
    }


    public Uni<List<Account>> listAllByUserId(Long userId) {
        return list("userId", userId);
    }
}
