package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.request.CreateAccountRequest;
import dev.econolyze.application.dto.request.UpdateAccountRequest;
import dev.econolyze.application.dto.response.AccountResponse;
import dev.econolyze.application.services.AccountService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;


@Path("/api/accounts")
public class AccountResource {
    @Inject
    AccountService accountService;

    @POST
    public RestResponse<AccountResponse> createAccount(CreateAccountRequest request){
        AccountResponse response = accountService.createAccount(request);
        return RestResponse.ok(response);
    }

    @PUT
    @Path("/{accountId}")
    public RestResponse<AccountResponse> updateAccount(@PathParam("accountId") Long accountId, UpdateAccountRequest request){
        AccountResponse response = accountService.updateAccount(request, accountId);
        return RestResponse.ok(response);
    }

    @DELETE
    @Path("/{accountId}")
    public RestResponse<Void> deleteAccount(@PathParam("accountId") Long accountId){
        accountService.deleteAccount(accountId);
        return RestResponse.noContent();
    }

    @GET
    @Path("/{accountId}")
    public RestResponse<AccountResponse> getAccountById(@PathParam("accountId") Long accountId){
        AccountResponse response = accountService.getAccountById(accountId);
        return RestResponse.ok(response);
    }

    @GET
    public RestResponse<List<AccountResponse>> getAllAccounts(){
        return RestResponse.ok(accountService.getAllAccounts());
    }
}
