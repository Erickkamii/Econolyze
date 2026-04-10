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
        return list("userId = ?1 and isActive = true", userId);
    }
}
