package dev.econolyze.interfaces.resource;

import dev.econolyze.domain.enums.Category;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;

@Path("/api/category")
public class CategoryResource {

    @GET
    public Response getAll() {
        List<String> categorias = Arrays.stream(Category.values())
                .map(Category::name)
                .toList();
        return Response.ok().entity(categorias).build();
    }

}
