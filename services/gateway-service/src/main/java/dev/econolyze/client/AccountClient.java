package dev.econolyze.client;

import dev.econolyze.dto.request.AccountRequest;
import dev.econolyze.dto.request.UpdateAccountRequest;
import dev.econolyze.dto.response.AccountResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api")
@RegisterRestClient(configKey = "financial-service")
@RegisterClientHeaders(AuthHeaderFactory.class)
public interface AccountClient {
    @POST
    @Path("/accounts")
    Uni<RestResponse<AccountResponse>> createAccount(
            @HeaderParam("Authorization") String authorization,
            AccountRequest request
    );

    @PUT
    @Path("/accounts/{accountId}")
    Uni<RestResponse<AccountResponse>> updateAccount(
            @HeaderParam("Authorization") String authorization,
            @PathParam("accountId") Long accountId,
            UpdateAccountRequest request
    );

    @DELETE
    @Path("/accounts/{accountId}")
    Uni<RestResponse<Void>> deleteAccount(
            @HeaderParam("Authorization") String authorization,
            @PathParam("accountId") Long accountId
    );

    @GET
    @Path("/accounts/{accountId}")
    Uni<RestResponse<AccountResponse>> getAccountById(
            @HeaderParam("Authorization") String authorization,
            @PathParam("accountId") Long accountId
    );

    @GET
    @Path("/accounts")
    Uni<RestResponse<List<AccountResponse>>> getAllAccounts(
            @HeaderParam("Authorization") String authorization
    );

}
