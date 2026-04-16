package dev.econolyze.resource;

import dev.econolyze.client.AuthServiceClient;
import dev.econolyze.dto.request.LoginRequest;
import dev.econolyze.dto.response.LoginResponse;
import dev.econolyze.dto.request.RegisterRequest;
import dev.econolyze.dto.response.RegisterResponse;
import dev.econolyze.exception.ServiceUnavailableException;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.jboss.resteasy.reactive.RestResponse;

@Slf4j
@Path("/api/auth")
public class AuthServiceResource {

    @Inject
    @RestClient
    AuthServiceClient authServiceClient;

    @POST
    @Path("/login")
    @PermitAll
    public Uni<RestResponse<LoginResponse>> login(LoginRequest request) {
        if (request == null || request.username() == null) {
            return Uni.createFrom().failure(new WebApplicationException("Request inválido", Response.Status.BAD_REQUEST));
        }
        log.info("Login attempt: username={}", request.username());
        return authServiceClient.login(request)
                .onFailure(ClientWebApplicationException.class).recoverWithUni(e -> {
                    int status = e.getResponse().getStatus();
                    if (status == 401) {
                        return Uni.createFrom().item(
                                RestResponse.status(RestResponse.Status.UNAUTHORIZED.getStatusCode(), "Credenciais inválidas"));
                    }
                    return Uni.createFrom().failure(e);
                })
                .onFailure().transform(e -> new ServiceUnavailableException("auth-service", e));
    }

    @POST
    @Path("/register")
    @PermitAll
    public Uni<RestResponse<RegisterResponse>> register(RegisterRequest request) {
        if (request == null || request.username() == null) {
            return Uni.createFrom().failure(new WebApplicationException("Request inválido", Response.Status.BAD_REQUEST));
        }
        log.info("Register attempt: username={}", request.username());
        return authServiceClient.register(request)
                .onFailure(ClientWebApplicationException.class).invoke(Unchecked.consumer(e -> {
                    if (e.getResponse().getStatus() == 409) {
                        throw new WebApplicationException("Username já existe", 409);
                    }
                }))
                .onFailure().invoke(e -> log.error("Error during register: {}", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("auth-service", e));
    }
    @POST
    @Path("/refresh")
    @PermitAll
    public Uni<RestResponse<LoginResponse>> refresh(@HeaderParam("Authorization") String token) {
        if (token == null || token.isBlank()) {
            return Uni.createFrom().failure(
                    new WebApplicationException("Authorization header ausente", Response.Status.UNAUTHORIZED));
        }
        log.info("Refresh token attempt");
        String headerValue = token.startsWith("Bearer ") ? token : "Bearer " + token;
        return authServiceClient.refresh(headerValue)
                .onFailure(ClientWebApplicationException.class).recoverWithUni(e -> {
                    int status = e.getResponse().getStatus();
                    if (status == 401) {
                        return Uni.createFrom().item(
                                RestResponse.status(RestResponse.Status.UNAUTHORIZED.getStatusCode(), "Token inválido ou expirado"));
                    }
                    return Uni.createFrom().failure(e);
                })
                .onFailure().transform(e -> new ServiceUnavailableException("auth-service", e));
    }

    @POST
    @Path("/logout")
    @PermitAll
    public Uni<RestResponse<Void>> logout(@HeaderParam("Authorization") String token) {
        String headerValue = token.startsWith("Bearer ") ? token : "Bearer " + token;
        return authServiceClient.logout(headerValue)
                .onFailure(ClientWebApplicationException.class).recoverWithUni(e -> {
                    if (((ClientWebApplicationException) e).getResponse().getStatus() == 401) {
                        return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
                    }
                    return Uni.createFrom().failure(e);
                })
                .onFailure().transform(e -> new ServiceUnavailableException("auth-service", e));
    }
}
