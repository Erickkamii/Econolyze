package dev.econolyze.interfaces.resource;


import dev.econolyze.application.dto.CdiRateDTO;
import dev.econolyze.application.services.CdiService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.jboss.resteasy.reactive.RestResponse;

import java.math.BigDecimal;

@Path("/api/cdi")
public class CdiResource {

    @Inject
    CdiService cdiService;

    @GET
    @Path("/rate")
    public RestResponse<BigDecimal> getCdiRate() {
        BigDecimal rate = cdiService.getCurrentCdiRate();
        return RestResponse.ok(rate);
    }

    @GET
    @Path("/rate/async")
    public Uni<RestResponse<BigDecimal>> getCdiRateAsync() {
        return cdiService.getCurrentCdiRateAsync()
                .onItem().transform(RestResponse::ok)
                .onFailure().recoverWithItem(RestResponse.serverError());
    }

    @GET
    @Path("/info")
    public RestResponse<CdiRateDTO> getCdiInfo() {
        CdiRateDTO info = cdiService.getCdiRateInfo();
        return RestResponse.ok(info);
    }

    @POST
    @Path("/refresh")
    public RestResponse<String> refreshCdiRate() {
        cdiService.updateCdiRate();
        return RestResponse.ok("CDI rate updated");
    }
}