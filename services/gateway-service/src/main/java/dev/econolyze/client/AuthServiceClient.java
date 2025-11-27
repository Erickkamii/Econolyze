package dev.econolyze.client;

import dev.econolyze.dto.request.LoginRequest;
import dev.econolyze.dto.response.LoginResponse;
import dev.econolyze.dto.request.RegisterRequest;
import dev.econolyze.dto.response.RegisterResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.HeaderParam;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/auth")
@RegisterRestClient(configKey = "auth-service")
public interface AuthServiceClient {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2, delay = 1000)
    RestResponse<LoginResponse> login(LoginRequest request);

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2, delay = 1000)
    RestResponse<RegisterResponse> register(RegisterRequest request);

    @POST
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(3000)
    RestResponse<LoginResponse> refresh(@HeaderParam("Authorization") String token);
}
