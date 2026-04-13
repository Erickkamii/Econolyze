package dev.econolyze.resource;

import dev.econolyze.client.InvestmentClient;
import dev.econolyze.dto.response.InvestmentProjectionResponse;
import dev.econolyze.exception.ServiceUnavailableException;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
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

    public boolean unauthorized(){
        return jwt.getClaim("userId") == null;
    }

    @GET
    @Path("/daily-cdi")
    public Uni<RestResponse<InvestmentProjectionResponse>> getDailyInvestmentProgress(@Context HttpHeaders headers, @RestQuery("percentage") BigDecimal percentage) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return investmentClient.getDailyInvestmentProgress(headers.getHeaderString("Authorization"), percentage)
                .onFailure().invoke(e -> LOG.errorf("Erro ao buscar projeção de investimento diário: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/monthly-cdi")
    public Uni<RestResponse<InvestmentProjectionResponse>> getMonthlyInvestmentProgress(@Context HttpHeaders headers, @RestQuery("percentage") BigDecimal percentage) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return investmentClient.getMonthlyInvestmentProgress(headers.getHeaderString("Authorization"), percentage)
                .onFailure().invoke(e -> LOG.errorf("Erro ao buscar projeção de investimento mensal: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/annual-cdi")
    public Uni<RestResponse<InvestmentProjectionResponse>> getAnnualInvestmentProgress(@Context HttpHeaders headers, @RestQuery("percentage") BigDecimal percentage) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return investmentClient.getAnnualInvestmentProgress(headers.getHeaderString("Authorization"), percentage)
                .onFailure().invoke(e -> LOG.errorf("Erro ao buscar projeção de investimento anual: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }
}
