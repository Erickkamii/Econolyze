package com.econolyze.dev.resource;

import com.econolyze.dev.dto.LoginRequestDTO;
import com.econolyze.dev.dto.LoginResponseDTO;
import com.econolyze.dev.dto.RegisterRequestDTO;
import com.econolyze.dev.entity.User;
import com.econolyze.dev.service.UserManager;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UserManager userManager;


    @POST
    @Path("/register")
    @PermitAll
    public Response register(@Valid RegisterRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.username);
        user.setEmail(dto.email);
        user.setPassword(dto.password);

        UserManager.addUser(user);
        return Response.ok("Seja bem vindo ao Econolyze! "+ user.getUsername()).build();
    }

    @GET
    @Path("/ping")
    public Response hello(){
        try {
            return Response.ok("pong!").build();
        }catch (Exception e){
            return Response.status(Response.Status.valueOf("HElP")).build();
        }
    }

    @POST
    @Path("/login")
    @PermitAll
    public Response login(LoginRequestDTO request) {
        try {
            LoginResponseDTO jwt = UserManager.login(request.username, request.password);
            return Response.ok().entity(jwt).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }


    @POST
    @RolesAllowed("admin")
    @Path("/admin/login")
    public Response adminLogin(@Context SecurityContext securityContext){
        return Response.ok(null).build();
    }

    @POST
    @Path("/refresh")
    @PermitAll
//    public Response refresh(@Context HttpServletRequest request, HttpServletResponse token)
    public Response refresh(@HeaderParam("Authorization") String token){
        try {
            LoginResponseDTO response = userManager.refreshTokenFromJWT();
            return Response.ok(response).build();
        } catch (WebApplicationException e) {
            return Response.status(e.getResponse().getStatus()).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Erro interno").build();
        }    }

}

