package dev.econolyze.client;

import dev.econolyze.dto.request.FinancialGoalRequest;
import dev.econolyze.dto.response.FinancialGoalResponse;
import dev.econolyze.dto.response.GoalProgressResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/goal")
@RegisterRestClient(configKey = "financial-service")
@RegisterClientHeaders(AuthHeaderFactory.class)
public interface GoalClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<FinancialGoalResponse> createGoal(
            @HeaderParam("Authorization") String authorization,
            FinancialGoalRequest goalDTO
    );
    @GET
    @Path("/{goalId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<FinancialGoalResponse> getGoalById(
            @HeaderParam("Authorization") String authorization,
            @PathParam("goalId") Long goalId
    );

    @GET
    @Path("/progress/{goalId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<GoalProgressResponse> getGoalProgress(
            @HeaderParam("Authorization") String authorization,
            @PathParam("goalId") Long goalId
    );
}