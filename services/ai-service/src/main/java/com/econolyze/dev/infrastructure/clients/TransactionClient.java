package com.econolyze.dev.infrastructure.clients;

import com.econolyze.dev.interfaces.rest.dto.TransactionByCategoryResponse;
import com.econolyze.dev.interfaces.rest.dto.TransactionDTO;
import com.econolyze.dev.interfaces.rest.dto.TransactionSummaryResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@RegisterRestClient(configKey = "financial-service")
@Path("/api/transaction")
public interface TransactionClient {

    @GET
    @Path("/internal/rag")
    Uni<RestResponse<List<TransactionDTO>>> getTransactionsByUserAndDate(
            @QueryParam("date") String date,
            @HeaderParam("X-Internal-Token") String internalToken
    );

    @GET
    @Path("/categories")
    Uni<RestResponse<List<TransactionByCategoryResponse>>> getTransactionsByCategory(
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("type") String type,
            @HeaderParam("Authorization") String authorization
    );

    @GET
    @Path("/summary")
    Uni<RestResponse<TransactionSummaryResponse>> getTransactionSummary(
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @HeaderParam("Authorization") String authorization
    );
}
