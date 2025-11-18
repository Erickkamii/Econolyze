package dev.econolyze.resource;

import dev.econolyze.client.GoalClient;
import dev.econolyze.dto.request.FinancialGoalRequest;
import dev.econolyze.dto.response.FinancialGoalResponse;
import dev.econolyze.dto.response.GoalProgressResponse;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/goal")
@Authenticated
public class GoalResource {
    private static final Logger LOG = Logger.getLogger(PaymentResource.class);

    @Inject
    @RestClient
    GoalClient goalClient;
    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/create")
    public RestResponse<FinancialGoalResponse> createGoal(@Context HttpHeaders headers, FinancialGoalRequest request) {
        if (jwt.getClaim("userId") == null){
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return goalClient.createGoal(authorization, request);
        } catch (Exception e) {
            LOG.errorf("Erro ao criar pagamento: %s", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/{goalId}")
    public RestResponse<FinancialGoalResponse> getGoalById(@Context HttpHeaders headers, @PathParam("goalId") Long goalId) {
        if (jwt.getClaim("userId") == null) {
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return goalClient.getGoalById(authorization, goalId);
        } catch (Exception e) {
            LOG.errorf("Erro ao buscar pagamento: %s", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/progress/{goalId}")
    public RestResponse<GoalProgressResponse> getGoalProgress(@Context HttpHeaders headers, @PathParam("goalId") Long goalId) {
        if (jwt.getClaim("userId") == null) {
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
            }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return goalClient.getGoalProgress(authorization, goalId);
        } catch (Exception e) {
            LOG.errorf("Erro ao buscar pagamento: %s", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
