package dev.econolyze.resource;

import dev.econolyze.dto.BalanceDTO;
import dev.econolyze.services.BalanceService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

@Path("/api/balance")
public class BalanceResource {

    @Inject
    BalanceService balanceService;
    @GET
    @Path("/{userId}")
    public BalanceDTO getBalanceByUserId(@PathParam("userId") Long userId) {
        return balanceService.getBalanceByUserId(userId);
    }
}
