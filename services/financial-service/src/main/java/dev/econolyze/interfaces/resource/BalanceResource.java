package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.response.BalanceResponse;
import dev.econolyze.application.services.BalanceService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/balance")
public class BalanceResource {

    private static final Logger LOG = Logger.getLogger(BalanceResource.class);

    @Inject
    BalanceService balanceService;

    @GET
    public RestResponse<BalanceResponse> getBalance() {
        return RestResponse.ok(balanceService.getBalanceByUserId());
    }
}
