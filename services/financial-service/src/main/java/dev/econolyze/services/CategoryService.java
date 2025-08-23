package dev.econolyze.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.econolyze.dto.CategoryDTO;
import dev.econolyze.entity.Category;
import dev.econolyze.exception.AlreadyExistsException;
import dev.econolyze.repository.CategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService {

    @Inject
    CategoryRepository categoryRepository;
    @Inject
    ObjectMapper objectMapper;

    public CategoryDTO addNewCategory(CategoryDTO categoryDTO) throws Exception {
        Category category = objectMapper.convertValue(categoryDTO, Category.class);
        if (categoryDontExists(categoryDTO.getName())){
            categoryRepository.persist(category);
        }
        else{
            throw new AlreadyExistsException("Category already exists");
        }
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .color(category.getColor()).build();
    }

    public List<CategoryDTO> listAll(){
        List<Category> categories = categoryRepository.listAll();
        return Collections.singletonList(objectMapper.convertValue(categories, CategoryDTO.class));
    }

    private boolean categoryDontExists(String name){
        return (categoryRepository.findCategoriesByName(name).isEmpty());
    }
}
