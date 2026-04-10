package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.request.CreateAccountRequest;
import dev.econolyze.application.dto.request.UpdateAccountRequest;
import dev.econolyze.application.dto.response.AccountResponse;
import dev.econolyze.application.services.AccountService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;


@Path("/api/accounts")
public class AccountResource {
    @Inject
    AccountService accountService;

    @POST
    public Uni<RestResponse<AccountResponse>> createAccount(CreateAccountRequest request){
        return accountService.createAccount(request)
                .map(RestResponse::ok);
    }

    @PUT
    @Path("/{accountId}")
    public Uni<RestResponse<AccountResponse>> updateAccount(@PathParam("accountId") Long accountId, UpdateAccountRequest request){
        return accountService.updateAccount(request, accountId).map(RestResponse::ok);
    }

    @DELETE
    @Path("/{accountId}")
    public Uni<RestResponse<Void>> deleteAccount(@PathParam("accountId") Long accountId){
        return accountService.deleteAccount(accountId).replaceWith(RestResponse::noContent);
    }

    @GET
    @Path("/{accountId}")
    public Uni<RestResponse<AccountResponse>> getAccountById(@PathParam("accountId") Long accountId){
        return accountService.getAccountById(accountId).map(RestResponse::ok);
    }

    @GET
    public Uni<RestResponse<List<AccountResponse>>> getAllAccounts(){
        return accountService.getAllAccounts().map(RestResponse::ok);
    }
}
