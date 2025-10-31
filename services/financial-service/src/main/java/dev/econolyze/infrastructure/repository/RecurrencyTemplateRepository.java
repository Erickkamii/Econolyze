package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.RecurringTemplate;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class RecurrencyTemplateRepository implements PanacheRepository<RecurringTemplate> {
    public List<RecurringTemplate> findActiveWithNextOccurrenceBefore(LocalDate today) {
        return list("nextOccurrence < ?1", today);
    }

    public List<RecurringTemplate> findActiveByUserId(Long userId) {
        return list("userId = ?1 and isActive = true", userId);
    }
}
