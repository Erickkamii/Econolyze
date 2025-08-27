package dev.econolyze.resource;

import dev.econolyze.dto.BalanceDTO;
import dev.econolyze.entity.Balance;
import dev.econolyze.services.BalanceService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/api/balance")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BalanceResource {

    @GET
    @Path("/{userId}")
    public BalanceDTO getBalanceByUserId(@PathParam("userId") Long userId) {
        return BalanceService.getBalanceByUserId(userId);
    }
}
