package dev.econolyze.infrastructure.config;


import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.Map;

@Path("/dados/serie/bcdata.sgs.4389/dados")
@RegisterRestClient(configKey = "banco-central-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BancoCentralConfig {

    @GET
    @Path("/ultimos/{quantidade}")
    Uni<List<Map<String, Object>>> getLatestCdiRate(
            @PathParam("quantidade") int quantidade,
            @QueryParam("formato") @DefaultValue("json") String formato
    );

    @GET
    @Path("/ultimos/{quantidade}")
    List<Map<String, Object>> getLatestCdiRateSync(
            @PathParam("quantidade") int quantidade,
            @QueryParam("formato") @DefaultValue("json") String formato
    );


}
