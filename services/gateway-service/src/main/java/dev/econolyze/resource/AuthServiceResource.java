package dev.econolyze.resource;

import dev.econolyze.client.AuthServiceClient;
import dev.econolyze.dto.LoginRequest;
import dev.econolyze.dto.LoginResponse;
import dev.econolyze.dto.RegisterRequest;
import dev.econolyze.exception.ServiceUnavailableException;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
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
    public RestResponse<LoginResponse> login(LoginRequest request) {
        logger.infof("Login attempt: %s", request.username());

        try{
            RestResponse<LoginResponse> login = authServiceClient.login(request);

            if (login.getStatus() == RestResponse.Status.UNAUTHORIZED.getStatusCode()) {
                return RestResponse.status(401, "Invalid credentials");
            }
            return login;
        } catch (Exception e){
            throw new ServiceUnavailableException("auth-service", e);
        }
    }

    @POST
    @Path("/register")
    @PermitAll
    public RestResponse<?> register(RegisterRequest request) {
        logger.infof("Register attempt: %s", request.username());

        try{
            RestResponse<LoginResponse> response = authServiceClient.register(request);
            if (response.getStatus() == RestResponse.Status.CONFLICT.getStatusCode()) {
                throw new WebApplicationException("Username already exists", 409);
            }
            return response;
        } catch (Exception e){
            logger.errorf("Error during register: %s", e.getMessage());
            throw new ServiceUnavailableException("auth-service", e);
        }
    }

    @POST
    @Path("/refresh")
    @PermitAll
    public RestResponse<LoginResponse> refresh(String token){
        logger.infof("Refresh token attempt: %s", token);

        try{
            return authServiceClient.refresh(token);
        } catch (Exception e){
            logger.errorf("Error during refresh: %s", e.getMessage());
            throw new ServiceUnavailableException("auth-service", e);
        }
    }
}
