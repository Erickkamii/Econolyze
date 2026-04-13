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
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/auth")
public class AuthServiceResource {
    private static final Logger logger = Logger.getLogger(AuthServiceResource.class);

    @Inject
    @RestClient
    AuthServiceClient authServiceClient;

    @POST
    @Path("/login")
    @PermitAll
    public Uni<RestResponse<LoginResponse>> login(LoginRequest request) {
        logger.infof("Login attempt: %s", request.username());
        return authServiceClient.login(request)
                .onFailure(ClientWebApplicationException.class).recoverWithUni(e -> {
                    if (((ClientWebApplicationException) e).getResponse().getStatus() == 401) {
                        return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED.getStatusCode(), "Invalid credentials"));
                    }
                    return Uni.createFrom().failure((ClientWebApplicationException) e);
                })
                .onFailure().transform(e -> new ServiceUnavailableException("auth-service", e));
    }

    @POST
    @Path("/register")
    @PermitAll
    public Uni<RestResponse<RegisterResponse>> register(RegisterRequest request) {
        logger.infof("Register attempt: %s", request.username());
        return authServiceClient.register(request)
                .onFailure(ClientWebApplicationException.class).invoke(Unchecked.consumer(e -> {
                    if (e.getResponse().getStatus() == 409) {
                        throw new WebApplicationException("Username already exists", 409);
                    }
                }))
                .onFailure().invoke(e -> logger.errorf("Error during register: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("auth-service", e));
    }

    @POST
    @Path("/refresh")
    @PermitAll
    public Uni<RestResponse<LoginResponse>> refresh(String token) {
        logger.infof("Refresh token attempt...");
        String headerValue = token.startsWith("Bearer ") ? token : "Bearer " + token;

        return authServiceClient.refresh(headerValue)
                .onFailure().transform(e -> new ServiceUnavailableException("auth-service", e));
    }
}
