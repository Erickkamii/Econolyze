package dev.econolyze.resource;

import dev.econolyze.client.AccountClient;
import dev.econolyze.dto.request.AccountRequest;
import dev.econolyze.dto.request.UpdateAccountRequest;
import dev.econolyze.dto.response.AccountResponse;
import dev.econolyze.exception.ServiceUnavailableException;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api/account")
@Authenticated
public class AccountResource {
    private static final Logger LOG = Logger.getLogger(TransactionResource.class);

    @Inject
    @RestClient
    AccountClient accountClient;
    @Inject
    JsonWebToken jwt;

    @POST
    public RestResponse<AccountResponse> createAccount(@Context HttpHeaders headers, AccountRequest request) {
        if (jwt.getClaim("userId") == null) {
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return accountClient.createAccount(authorization, request);
        } catch (Exception e) {
            LOG.errorf("Erro ao criar conta: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @GET
    @Path("/{accountId}")
    public RestResponse<AccountResponse> getAccountById(@Context HttpHeaders headers, @PathParam("accountId") Long accountId) {
        if (jwt.getClaim("userId") == null) {
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
            }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return accountClient.getAccountById(authorization, accountId);
        } catch (Exception e) {
            LOG.errorf("Erro ao buscar conta: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @GET
    public RestResponse<List<AccountResponse>> getAllAccounts(@Context HttpHeaders headers) {
        if (jwt.getClaim("userId") == null) {
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return accountClient.getAllAccounts(authorization);
        } catch (Exception e) {
            LOG.errorf("Erro ao buscar contas: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @PUT
    @Path("/{accountId}")
    public RestResponse<AccountResponse> updateAccount(@Context HttpHeaders headers, @PathParam("accountId") Long accountId, UpdateAccountRequest request) {
        if (jwt.getClaim("userId") == null) {
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return accountClient.updateAccount(authorization, accountId, request);
        } catch (Exception e) {
            LOG.errorf("Erro ao atualizar conta: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @DELETE
    @Path("/{accountId}")
    public RestResponse<Void> deleteAccount(@Context HttpHeaders headers, @PathParam("accountId") Long accountId) {
        if (jwt.getClaim("userId") == null) {
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return accountClient.deleteAccount(authorization, accountId);
        } catch (Exception e) {
            LOG.errorf("Erro ao deletar conta: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

}
