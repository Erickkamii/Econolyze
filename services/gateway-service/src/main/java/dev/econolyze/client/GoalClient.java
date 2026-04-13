package dev.econolyze.client;

import dev.econolyze.dto.request.FinancialGoalRequest;
import dev.econolyze.dto.response.FinancialGoalResponse;
import dev.econolyze.dto.response.GoalProgressResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api/goal")
@RegisterRestClient(configKey = "financial-service")
@RegisterClientHeaders(AuthHeaderFactory.class)
public interface GoalClient {

    @POST
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<FinancialGoalResponse>> createGoal(
            @HeaderParam("Authorization") String authorization,
            FinancialGoalRequest goalDTO
    );

    @GET
    @Path("/{goalId}")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<FinancialGoalResponse>> getGoalById(
            @HeaderParam("Authorization") String authorization,
            @PathParam("goalId") Long goalId
    );

    @GET
    @Path("/progress/{goalId}")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<GoalProgressResponse>> getGoalProgress(
            @HeaderParam("Authorization") String authorization,
            @PathParam("goalId") Long goalId
    );

    @GET
    @Path("/all")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<List<FinancialGoalResponse>>> getAllGoals(
            @HeaderParam("Authorization") String authorization);
}