package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.FinancialGoal;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FinancialGoalRepository implements PanacheRepository<FinancialGoal> {
}
