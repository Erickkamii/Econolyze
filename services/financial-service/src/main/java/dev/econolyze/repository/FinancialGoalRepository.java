package dev.econolyze.repository;

import dev.econolyze.entity.FinancialGoal;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FinancialGoalRepository implements PanacheRepository<FinancialGoal> {
}
