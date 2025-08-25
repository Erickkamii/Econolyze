package dev.econolyze.repository;

import dev.econolyze.entity.Income;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IncomeRepository implements PanacheRepository<Income> {
}
