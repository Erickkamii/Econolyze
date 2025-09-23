package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.entity.Income;
import dev.econolyze.domain.enums.Category;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class IncomeRepository implements PanacheRepository<Income> {
    public List<Income> findAllIncomesByUserId(Long userId) {
        return list("userId", userId);
    }

    public List<Income> findAllIncomesByUserIdAndCategory(Long userId, String categoryName){
        try {
            Category category = Category.fromString(categoryName);
            Integer categoryId = category.getCode();

            return list("userId = ?1 and categoryId = ?2", userId, categoryId);

        } catch (IllegalArgumentException e) {
            System.err.println("Invalid category: " + categoryName);
            return List.of();
        }
    }
}
