package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.FinancialGoal;
import dev.econolyze.domain.enums.GoalStatus;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FinancialGoalRepository implements PanacheRepository<FinancialGoal> {

    public Uni<List<FinancialGoal>> getAllGoalsByUserId(Long userId){
            return list("userId", userId);
    }

    public Uni<List<FinancialGoal>> findActiveByUserId(Long userId) {
        return find("""
            userId = ?1
            and status = ?2
            """, userId, GoalStatus.ACTIVE)
                .list();
    }

    public Uni<List<FinancialGoal>> findByName(Long userId, String name) {
        return find("""
                userId = ?1
                and name = ?2
                """, userId, name
        ).list();
    }
}
