package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.application.services.BalanceService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/api/balance")
public class BalanceResource {

    @Inject
    BalanceService balanceService;
    @GET
    @Path("/{userId}")
    public Response getBalanceByUserId(@PathParam("userId") Long userId) {
        BalanceDTO balanceDTO = balanceService.getBalanceByUserId(userId);
        return Response.ok().entity(balanceDTO).build();
    }
}
