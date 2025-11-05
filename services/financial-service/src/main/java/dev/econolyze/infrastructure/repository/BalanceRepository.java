package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.Balance;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;

@ApplicationScoped
public class BalanceRepository implements PanacheRepository<Balance> {
    public Balance findByUserIdForUpdate(Long userId) {
        return find("userId", userId)
                .withLock(LockModeType.PESSIMISTIC_WRITE)
                .firstResult();
    }
}
