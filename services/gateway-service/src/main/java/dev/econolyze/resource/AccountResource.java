package dev.econolyze.resource;

import dev.econolyze.client.AccountClient;
import dev.econolyze.dto.request.AccountRequest;
import dev.econolyze.dto.request.UpdateAccountRequest;
import dev.econolyze.dto.response.AccountResponse;
import dev.econolyze.exception.ServiceUnavailableException;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
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

    private boolean unauthorized(){
        return jwt.getClaim("userId") == null;
    }

    @POST
    public Uni<RestResponse<AccountResponse>> createAccount(@Context HttpHeaders headers, AccountRequest request) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return accountClient.createAccount(headers.getHeaderString("Authorization"), request)
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar conta: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/{accountId}")
    public Uni<RestResponse<AccountResponse>> getAccountById(@Context HttpHeaders headers, @PathParam("accountId") Long accountId) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return accountClient.getAccountById(headers.getHeaderString("Authorization"), accountId)
                .onFailure().invoke(e -> LOG.errorf("Erro ao buscar conta: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    public Uni<RestResponse<List<AccountResponse>>> getAllAccounts(@Context HttpHeaders headers) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return accountClient.getAllAccounts(headers.getHeaderString("Authorization"))
                .onFailure().invoke(e -> LOG.errorf("Erro ao buscar contas: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @PUT
    @Path("/{accountId}")
    public Uni<RestResponse<AccountResponse>> updateAccount(@Context HttpHeaders headers, @PathParam("accountId") Long accountId, UpdateAccountRequest request) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return accountClient.updateAccount(headers.getHeaderString("Authorization"), accountId, request)
                .onFailure().invoke(e -> LOG.errorf("Erro ao atualizar conta: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @DELETE
    @Path("/{accountId}")
    public Uni<RestResponse<Void>> deleteAccount(@Context HttpHeaders headers, @PathParam("accountId") Long accountId) {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return accountClient.deleteAccount(headers.getHeaderString("Authorization"), accountId)
                .onFailure().invoke(e -> LOG.errorf("Erro ao deletar conta: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

}
