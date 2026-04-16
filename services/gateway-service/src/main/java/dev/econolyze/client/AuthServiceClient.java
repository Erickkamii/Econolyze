package dev.econolyze.client;

import dev.econolyze.dto.request.LoginRequest;
import dev.econolyze.dto.response.LoginResponse;
import dev.econolyze.dto.request.RegisterRequest;
import dev.econolyze.dto.response.RegisterResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
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
    @Timeout(5000)
    @Retry(maxRetries = 2, delay = 1000)
    Uni<RestResponse<LoginResponse>> login(LoginRequest request);

    @POST
    @Path("/register")
    @Timeout(5000)
    @Retry(maxRetries = 2, delay = 1000)
    Uni<RestResponse<RegisterResponse>> register(RegisterRequest request);

    @POST
    @Path("/refresh")
    @Timeout(3000)
    Uni<RestResponse<LoginResponse>> refresh(@HeaderParam("Authorization") String token);

    @POST
    @Path("/logout")
    @Timeout(3000)
    Uni<RestResponse<Void>> logout(@HeaderParam("Authorization") String token);
}
