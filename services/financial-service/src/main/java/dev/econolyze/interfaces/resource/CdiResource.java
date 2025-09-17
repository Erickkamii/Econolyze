package dev.econolyze.interfaces.resource;


import dev.econolyze.application.services.CdiService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;

import java.math.BigDecimal;

@Path("/api/cdi")
public class CdiResource {
    @Inject
    CdiService cdiService;
    @GET
    public RestResponse<BigDecimal> getCdiRate() {
        BigDecimal cdiRate = cdiService.getCurrentCdiRate();
        return RestResponse.ok(cdiRate);
    }

}
