package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.RecurringTemplate;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class RecurrencyTemplateRepository implements PanacheRepository<RecurringTemplate> {
    public Uni<List<RecurringTemplate>> findActiveWithNextOccurrenceBefore(LocalDate today) {
        return list("nextOccurrence < ?1", today);
    }

    public Uni<List<RecurringTemplate>> findActiveByUserId(Long userId) {
        return find("SELECT DISTINCT r FROM RecurringTemplate r LEFT JOIN FETCH r.transactions WHERE r.userId = ?1", userId).list();
    }

    public Uni<RecurringTemplate> findByIdWithTransactions(Long id) {
        return find("SELECT DISTINCT r FROM RecurringTemplate r LEFT JOIN FETCH r.transactions WHERE r.id = ?1", id).firstResult();
    }
}
