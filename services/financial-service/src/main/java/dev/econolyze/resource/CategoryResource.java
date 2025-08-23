package dev.econolyze.resource;

import dev.econolyze.dto.CategoryDTO;
import dev.econolyze.services.CategoryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/category")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Inject
    CategoryService categoryService;

    @GET
    public List<CategoryDTO> getAll() {
        return categoryService.listAll();
    }

    @POST
    public CategoryDTO addCategory(CategoryDTO category) {
        return categoryService.addNewCategory(category);
    }
}
