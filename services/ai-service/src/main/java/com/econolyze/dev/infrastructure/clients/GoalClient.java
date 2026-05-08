package com.econolyze.dev.infrastructure.clients;

import com.econolyze.dev.interfaces.rest.dto.GoalDTO;
import com.econolyze.dev.interfaces.rest.dto.InvestmentProjectionDTO;
import com.econolyze.dev.interfaces.rest.dto.InvestmentProjectionRequest;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@RegisterRestClient(configKey = "financial-service")
@Path("/api/goal")
public interface GoalClient {

    @GET
    @Path("/active")
    Uni<RestResponse<List<GoalDTO>>> getActiveGoals(
        @HeaderParam("Authorization") String authorization
    );

    @GET
    @Path("/name")
    Uni<RestResponse<List<GoalDTO>>> getGoalsByName(
            @QueryParam("name") String name,
            @HeaderParam("Authorization") String authorization
    );

    @POST
    @Path("/projection")
    Uni<RestResponse<InvestmentProjectionDTO>> simulateProjection(
            InvestmentProjectionRequest request,
            @HeaderParam("Authorization") String authorization
    );
}
