package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.application.services.BalanceService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.logging.Logger;

@Path("/api/balance")
public class BalanceResource {

    private static final Logger LOG = Logger.getLogger(BalanceResource.class);

    @Inject
    BalanceService balanceService;
    @GET
    public RestResponse<BalanceDTO> getBalanceByUserId(@HeaderParam("X-User-Id") Long userId) {
        LOG.infof("Recebido X-User-Id: %s", userId);
        if (userId == null) {
            LOG.error("Header X-User-Id não recebido ou inválido");
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        BalanceDTO balanceDTO = balanceService.getBalanceByUserId(userId);
        return RestResponse.ok(balanceDTO);
    }
}
