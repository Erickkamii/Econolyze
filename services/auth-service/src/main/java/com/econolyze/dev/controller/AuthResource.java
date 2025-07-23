package com.econolyze.dev.controller;

import com.econolyze.dev.model.User;
import com.econolyze.dev.util.UserManager;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/auth")
public class AuthResource {
    @POST
    @PermitAll
    @Path("/register")
    public Response register(User user){
        UserManager.addUser(user);
        return Response.ok(null).build();
    }

    @POST
    @RolesAllowed("user")
    @Path("/login")
    public String login(@Context SecurityContext securityContext){
        return UserManager.generateJWT(securityContext.getUserPrincipal().getName());
    }

    @POST
    @RolesAllowed("admin")
    @Path("/admin/login")
    public Response adminLogin(@Context SecurityContext securityContext){
        return Response.ok(null).build();
    }
}
