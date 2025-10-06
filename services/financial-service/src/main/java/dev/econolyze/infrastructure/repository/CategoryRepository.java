package dev.econolyze.infrastructure.repository;

import dev.econolyze.domain.enums.Category;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {

    public List<Category> findCategoriesByName(String name){
        return list("name", name);
    }

}
