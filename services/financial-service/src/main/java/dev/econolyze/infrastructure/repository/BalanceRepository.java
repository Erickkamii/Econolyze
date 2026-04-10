package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.Balance;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;

@ApplicationScoped
public class BalanceRepository implements PanacheRepository<Balance> {
    public Uni<Balance> findByUserIdForUpdate(Long userId) {
        return find("userId", userId)
                .withLock(LockModeType.PESSIMISTIC_WRITE)
                .firstResult();
    }
}
