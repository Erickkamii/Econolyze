package com.econolyze.dev.resource;

import com.econolyze.dev.dto.LoginRequestDTO;
import com.econolyze.dev.dto.LoginResponseDTO;
import com.econolyze.dev.dto.RegisterRequestDTO;
import com.econolyze.dev.entity.User;
import com.econolyze.dev.exception.InvalidTokenException;
import com.econolyze.dev.exception.WrongCredentialsException;
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
import org.jboss.resteasy.reactive.RestResponse;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UserManager userManager;


    @POST
    @Path("/register")
    @PermitAll
    public RestResponse<String> register(@Valid RegisterRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.username);
        user.setEmail(dto.email);
        user.setPassword(dto.password);

        UserManager.addUser(user);
        return RestResponse.ok("Seja bem vindo ao Econolyze! "+ user.getUsername());
    }

    @GET
    @Path("/ping")
    public RestResponse<String> hello(){
        try {
            return RestResponse.ok("pong!");
        }catch (Exception e){
            return RestResponse.status(RestResponse.Status.valueOf("HElP"));
        }
    }

    @POST
    @Path("/login")
    @PermitAll
    public RestResponse<LoginResponseDTO> login(LoginRequestDTO request) {
        try {
            LoginResponseDTO jwt = UserManager.login(request.username, request.password);
            return RestResponse.ok(jwt);
        } catch (IllegalArgumentException e) {
            throw new WrongCredentialsException(e.getMessage());
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
    public RestResponse<LoginResponseDTO> refresh(@HeaderParam("Authorization") String token){
        try {
            LoginResponseDTO response = userManager.refreshTokenFromJWT();
            return RestResponse.ok(response);
            } catch (WebApplicationException e) {
                throw new InvalidTokenException(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException("Erro interno", e);
            }
        }
}



