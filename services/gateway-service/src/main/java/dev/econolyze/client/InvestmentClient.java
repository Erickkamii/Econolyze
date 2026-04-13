package dev.econolyze.client;

import dev.econolyze.dto.response.InvestmentProjectionResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;

import java.math.BigDecimal;

@Path("/api")
@RegisterRestClient(configKey = "financial-service")
@RegisterClientHeaders(AuthHeaderFactory.class)
public interface InvestmentClient {

    @GET
    @Path("/goal/daily-cdi")
    Uni<RestResponse<InvestmentProjectionResponse>> getDailyInvestmentProgress(
            @HeaderParam("Authorization") String authorization,
            @RestQuery("percentage") BigDecimal percentage
    );
    @GET
    @Path("/goal/monthly-cdi")
    Uni<RestResponse<InvestmentProjectionResponse>> getMonthlyInvestmentProgress(
            @HeaderParam("Authorization") String authorization,
            @RestQuery("percentage") BigDecimal percentage
    );
    @GET
    @Path("/goal/annual-cdi")
    Uni<RestResponse<InvestmentProjectionResponse>> getAnnualInvestmentProgress(
            @HeaderParam("Authorization") String authorization,
            @RestQuery("percentage") BigDecimal percentage
    );
}
