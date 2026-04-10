package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.FinancialGoal;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FinancialGoalRepository implements PanacheRepository<FinancialGoal> {

    public Uni<List<FinancialGoal>> getAllGoalsByUserId(Long userId){
            return list("userId", userId);
    }
}
