package dev.econolyze.repository;

import dev.econolyze.dto.CategoryDTO;
import dev.econolyze.entity.Category;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {

    public List<Category> findCategoriesByName(String name){
        return list("name", name);
    }

}
