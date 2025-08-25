package dev.econolyze.repository;

import dev.econolyze.entity.Expense;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseRepository implements PanacheRepository<Expense> {
}
