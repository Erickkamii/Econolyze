package dev.econolyze.repository;

import dev.econolyze.entity.Expense;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ExpenseRepository implements PanacheRepository<Expense> {
    public List<Expense> findAllExpensesByUserId(Long userId){
        return list("userId", userId);
    }
}
