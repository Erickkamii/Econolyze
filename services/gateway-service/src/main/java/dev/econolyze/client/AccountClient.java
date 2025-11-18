package dev.econolyze.client;

import dev.econolyze.dto.request.AccountRequest;
import dev.econolyze.dto.request.UpdateAccountRequest;
import dev.econolyze.dto.response.AccountResponse;
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
    RestResponse<AccountResponse> createAccount(
            @HeaderParam("Authorization") String authorization,
            AccountRequest request
    );

    @PUT
    @Path("/accounts/{accountId}")
    RestResponse<AccountResponse> updateAccount(
            @HeaderParam("Authorization") String authorization,
            @PathParam("accountId") Long accountId,
            UpdateAccountRequest request
    );

    @DELETE
    @Path("/accounts/{accountId}")
    RestResponse<Void> deleteAccount(
            @HeaderParam("Authorization") String authorization,
            @PathParam("accountId") Long accountId
    );

    @GET
    @Path("/accounts/{accountId}")
    RestResponse<AccountResponse> getAccountById(
            @HeaderParam("Authorization") String authorization,
            @PathParam("accountId") Long accountId
    );

    @GET
    @Path("/accounts")
    RestResponse<List<AccountResponse>> getAllAccounts(
            @HeaderParam("Authorization") String authorization
    );

}
