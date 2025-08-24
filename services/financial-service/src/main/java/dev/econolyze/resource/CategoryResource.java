package dev.econolyze.resource;

import dev.econolyze.enums.Category;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/category")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @GET
    public List<String> getAll() {
        return Arrays.stream(Category.values())
                .map(Category::name)
                .collect(Collectors.toList());
    }

}
