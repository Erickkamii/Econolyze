package dev.econolyze.repository;

import dev.econolyze.entity.Income;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class IncomeRepository implements PanacheRepository<Income> {
    public List<Income> findAllIncomesByUserId(Long userId) {
        return list("userId", userId);
    }
}
