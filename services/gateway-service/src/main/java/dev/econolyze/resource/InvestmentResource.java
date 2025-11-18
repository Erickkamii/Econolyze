package dev.econolyze.resource;

import dev.econolyze.client.InvestmentClient;
import dev.econolyze.dto.response.InvestmentProjectionResponse;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;

import java.math.BigDecimal;

@Path("/api/investment")
@Authenticated
public class InvestmentResource {
    private static final Logger LOG = Logger.getLogger(PaymentResource.class);

    @Inject
    @RestClient
    InvestmentClient investmentClient;
    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/daily-cdi")
    public RestResponse<InvestmentProjectionResponse> getDailyInvestmentProgress(@Context HttpHeaders headers, @RestQuery("percentage") BigDecimal percentage) {
        if (jwt.getClaim("userId") == null) {
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return investmentClient.getDailyInvestmentProgress(authorization, percentage);
        } catch (Exception e) {
            LOG.errorf("Erro ao buscar projeção de investimento diária: %s", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/monthly-cdi")
    public RestResponse<InvestmentProjectionResponse> getMonthlyInvestmentProgress(@Context HttpHeaders headers, @RestQuery("percentage") BigDecimal percentage) {
        if (jwt.getClaim("userId") == null) {
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return investmentClient.getMonthlyInvestmentProgress(authorization, percentage);
        } catch (Exception e) {
            LOG.errorf("Erro ao buscar projeção de investimento mensal: %s", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/annual-cdi")
    public RestResponse<InvestmentProjectionResponse> getAnnualInvestmentProgress(@Context HttpHeaders headers, @RestQuery("percentage") BigDecimal percentage) {
        if (jwt.getClaim("userId") == null) {
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return investmentClient.getAnnualInvestmentProgress(authorization, percentage);
        } catch (Exception e) {
            LOG.errorf("Erro ao buscar projeção de investimento anual: %s", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
