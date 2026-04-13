package dev.econolyze.resource;

import dev.econolyze.client.GoalClient;
import dev.econolyze.dto.request.FinancialGoalRequest;
import dev.econolyze.dto.response.FinancialGoalResponse;
import dev.econolyze.dto.response.GoalProgressResponse;
import dev.econolyze.exception.ServiceUnavailableException;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
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

    private boolean unauthorized(){
        return jwt.getClaim("userId") == null;
    }

    @POST
    @Path("/create")
    public Uni<RestResponse<FinancialGoalResponse>> createGoal(@Context HttpHeaders headers, FinancialGoalRequest request) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return goalClient.createGoal(headers.getHeaderString("Authorization"), request)
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar meta: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/{goalId}")
    public Uni<RestResponse<FinancialGoalResponse>> getGoalById(@Context HttpHeaders headers, @PathParam("goalId") Long goalId) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return goalClient.getGoalById(headers.getHeaderString("Authorization"), goalId)
                .onFailure().invoke(e -> LOG.errorf("Erro ao buscar meta: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/progress/{goalId}")
    public Uni<RestResponse<GoalProgressResponse>> getGoalProgress(@Context HttpHeaders headers, @PathParam("goalId") Long goalId) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return goalClient.getGoalProgress(headers.getHeaderString("Authorization"), goalId)
                .onFailure().invoke(e -> LOG.errorf("Erro ao buscar progresso da meta: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }
}
