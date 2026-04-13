package dev.econolyze.resource;

import dev.econolyze.client.RecurrencyClient;
import dev.econolyze.dto.request.RecurringTemplateRequest;
import dev.econolyze.dto.request.UpdateRecurringRequest;
import dev.econolyze.dto.response.PagedResponse;
import dev.econolyze.dto.response.RecurringTemplateResponse;
import dev.econolyze.dto.response.RecurringTemplateSummaryResponse;
import dev.econolyze.dto.response.TransactionResponse;
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

import java.time.LocalDate;
import java.util.List;

@Path("/api/financial")
@Authenticated
public class RecurringTemplateResource {

    private static final Logger LOG = Logger.getLogger(TransactionResource.class);

    @Inject
    @RestClient
    RecurrencyClient recurrencyClient;
    @Inject
    JsonWebToken jwt;

    private boolean unauthorized(){
        return jwt.getClaim("userId") == null;
    }

    @POST
    @Path("/recurring")
    public Uni<RestResponse<RecurringTemplateResponse>> createRecurring(@Context HttpHeaders headers,
                                                                        RecurringTemplateRequest request)
    {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return recurrencyClient.createRecurringTemplate(headers.getHeaderString("Authorization"), request)
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar transação: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @PUT
    @Path("/recurring/{templateId}")
    public Uni<RestResponse<RecurringTemplateResponse>> updateRecurring(@Context HttpHeaders headers,
                                                                   UpdateRecurringRequest request,
                                                                   @PathParam("templateId") Long templateId)
    {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return recurrencyClient.updateRecurringTemplate(headers.getHeaderString("Authorization"), templateId, request)
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar transação: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @PATCH
    @Path("/recurring/{templateId}/toggle")
    public Uni<RestResponse<RecurringTemplateResponse>> toggleRecurring(@Context HttpHeaders headers,
                                                                   @PathParam("templateId") Long templateId)
    {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return recurrencyClient.toggleRecurring(headers.getHeaderString("Authorization"), templateId)
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar transação: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/recurring")
    public Uni<RestResponse<List<RecurringTemplateResponse>>> getAllRecurring(@Context HttpHeaders headers)
    {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return recurrencyClient.getAllRecurringTemplates(headers.getHeaderString("Authorization"))
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar transação: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/recurring/{templateId}")
    public Uni<RestResponse<RecurringTemplateResponse>> getRecurringById(@Context HttpHeaders headers,
                                                                   @PathParam("templateId") Long templateId)
    {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return recurrencyClient.getRecurringTemplateById(headers.getHeaderString("Authorization"), templateId)
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar transação: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/recurring/{templateId}/history")
    public Uni<RestResponse<PagedResponse<TransactionResponse>>> getRecurringHistory(@Context HttpHeaders httpHeaders,
                                                                 @PathParam("templateId") Long templateId){
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return recurrencyClient.getRecurringHistory(httpHeaders.getHeaderString("Authorization"), templateId)
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar transação: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/recurring/{templateId}/summary")
    public Uni<RestResponse<RecurringTemplateSummaryResponse>> getRecurringSummary(@Context HttpHeaders headers,
                                                                              @PathParam("templateId") Long templateId){
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return recurrencyClient.getRecurringSummary(headers.getHeaderString("Authorization"), templateId)
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar transação: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/recurring/{templateId}/preview")
    public Uni<RestResponse<PagedResponse<LocalDate>>> getRecurringPreview(@Context HttpHeaders headers,
                                                                      @PathParam("templateId") Long templateId,
                                                                      @QueryParam("page") @DefaultValue("0") int page,
                                                                      @QueryParam("pageSize") @DefaultValue("20") int pageSize)
    {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return recurrencyClient.getRecurringPreview(headers.getHeaderString("Authorization"), templateId, page, pageSize)
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar transação: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/recurring/{templateId}/preview/full")
    public Uni<RestResponse<PagedResponse<LocalDate>>> getRecurringPreviewFull(@Context HttpHeaders headers,
                                                                          @PathParam("templateId") Long templateId,
                                                                          @QueryParam("page") @DefaultValue("0") int page,
                                                                          @QueryParam("pageSize") @DefaultValue("20") int pageSize)
    {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return recurrencyClient.getRecurringPreviewFull(headers.getHeaderString("Authorization"), templateId, page, pageSize)
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar transação: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @DELETE
    @Path("/recurring/{templateId}")
    public Uni<RestResponse<Void>> deleteRecurring(@Context HttpHeaders headers,
                                              @PathParam("templateId") Long templateId)
    {
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return recurrencyClient.deleteRecurringTemplate(headers.getHeaderString("Authorization"), templateId)
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar transação: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

}
