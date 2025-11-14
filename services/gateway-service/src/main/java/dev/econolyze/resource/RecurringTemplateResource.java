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
import jakarta.inject.Inject;
import jakarta.json.JsonNumber;
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

    @POST
    @Path("/recurring")
    public RestResponse<RecurringTemplateResponse> createRecurring(@Context HttpHeaders headers,
                                                                   RecurringTemplateRequest request)
    {
        JsonNumber userIdClaim = jwt.getClaim("userId");
        String authorization = headers.getHeaderString("Authorization");
        if (userIdClaim == null) {
            throw new ServiceUnavailableException("financial-service");
        }
        try {
            return recurrencyClient.createRecurringTemplate(authorization, request);
        } catch (Exception e) {
            LOG.errorf("Erro ao criar transação: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @PUT
    @Path("/recurring/{templateId}")
    public RestResponse<RecurringTemplateResponse> updateRecurring(@Context HttpHeaders headers,
                                                                   UpdateRecurringRequest request,
                                                                   @PathParam("templateId") Long templateId)
    {
        JsonNumber userIdClaim = jwt.getClaim("userId");
        String authorization = headers.getHeaderString("Authorization");
        if (userIdClaim == null) {
            throw new ServiceUnavailableException("financial-service");
        }
        try {
            return recurrencyClient.updateRecurringTemplate(authorization, templateId, request);
            } catch (Exception e) {
            LOG.errorf("Erro ao criar transação: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @PATCH
    @Path("/recurring/{templateId}/toggle")
    public RestResponse<RecurringTemplateResponse> toggleRecurring(@Context HttpHeaders headers,
                                                                   @PathParam("templateId") Long templateId)
    {
        JsonNumber userIdClaim = jwt.getClaim("userId");
        String authorization = headers.getHeaderString("Authorization");
        if (userIdClaim == null) {
            throw new ServiceUnavailableException("financial-service");
        }
        try {
            return recurrencyClient.toggleRecurring(authorization, templateId);
        } catch (Exception e) {
            LOG.errorf("Erro ao criar transação: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @GET
    @Path("/recurring")
    public RestResponse<List<RecurringTemplateResponse>> getAllRecurring(@Context HttpHeaders headers)
    {
        JsonNumber userIdClaim = jwt.getClaim("userId");
        String authorization = headers.getHeaderString("Authorization");
        if (userIdClaim == null) {
            throw new ServiceUnavailableException("financial-service");
        }
        try {
            return recurrencyClient.getAllRecurringTemplates(authorization);
        } catch (Exception e) {
            LOG.errorf("Erro ao criar transação: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @GET
    @Path("/recurring/{templateId}")
    public RestResponse<RecurringTemplateResponse> getRecurringById(@Context HttpHeaders headers,
                                                                   @PathParam("templateId") Long templateId)
    {
        JsonNumber userIdClaim = jwt.getClaim("userId");
        String authorization = headers.getHeaderString("Authorization");
        if (userIdClaim == null) {
            throw new ServiceUnavailableException("financial-service");
        }
        try {
            return recurrencyClient.getRecurringTemplateById(authorization, templateId);
        }
        catch (Exception e) {
            LOG.errorf("Erro ao criar transação: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service");
        }
    }

    @GET
    @Path("/recurring/{templateId}/history")
    public RestResponse<PagedResponse<TransactionResponse>> getRecurringHistory(@Context HttpHeaders httpHeaders,
                                                                 @PathParam("templateId") Long templateId){
        JsonNumber userIdClaim = jwt.getClaim("userId");
        String authorization = httpHeaders.getHeaderString("Authorization");
        if (userIdClaim == null) {
            throw new ServiceUnavailableException("financial-service");
        }
        try {
            return recurrencyClient.getRecurringHistory(authorization, templateId);
        } catch (Exception e) {
            LOG.errorf("Erro ao criar recorrencia: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @GET
    @Path("/recurring/{templateId}/summary")
    public RestResponse<RecurringTemplateSummaryResponse> getRecurringSummary(@Context HttpHeaders headers,
                                                                              @PathParam("templateId") Long templateId){
        JsonNumber userIdClaim = jwt.getClaim("userId");
        String authorization = headers.getHeaderString("Authorization");
        if (userIdClaim == null) {
            throw new ServiceUnavailableException("financial-service");
        }
        try {
            return recurrencyClient.getRecurringSummary(authorization, templateId);
        } catch (Exception e) {
            LOG.errorf("Erro ao criar recorrencia: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @GET
    @Path("/recurring/{templateId}/preview")
    public RestResponse<PagedResponse<LocalDate>> getRecurringPreview(@Context HttpHeaders headers,
                                                                      @PathParam("templateId") Long templateId,
                                                                      @QueryParam("page") @DefaultValue("0") int page,
                                                                      @QueryParam("pageSize") @DefaultValue("20") int pageSize)
    {
        JsonNumber userIdClaim = jwt.getClaim("userId");
        String authorization = headers.getHeaderString("Authorization");
        if (userIdClaim == null) {
            throw new ServiceUnavailableException("financial-service");
        }
        try {
            return recurrencyClient.getRecurringPreview(authorization, templateId, page, pageSize);
        } catch (Exception e) {
            LOG.errorf("Erro ao criar recorrencia: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @GET
    @Path("/recurring/{templateId}/preview/full")
    public RestResponse<PagedResponse<LocalDate>> getRecurringPreviewFull(@Context HttpHeaders headers,
                                                                          @PathParam("templateId") Long templateId,
                                                                          @QueryParam("page") @DefaultValue("0") int page,
                                                                          @QueryParam("pageSize") @DefaultValue("20") int pageSize)
    {
        JsonNumber userIdClaim = jwt.getClaim("userId");
        String authorization = headers.getHeaderString("Authorization");
        if (userIdClaim == null) {
            throw new ServiceUnavailableException("financial-service");
        }
        try {
            return recurrencyClient.getRecurringPreviewFull(authorization, templateId, page, pageSize);
        } catch (Exception e) {
            LOG.errorf("Erro ao criar recorrencia: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

    @DELETE
    @Path("/recurring/{templateId}")
    public RestResponse<Void> deleteRecurring(@Context HttpHeaders headers,
                                              @PathParam("templateId") Long templateId)
    {
        JsonNumber userIdClaim = jwt.getClaim("userId");
        String authorization = headers.getHeaderString("Authorization");
        if (userIdClaim == null) {
            throw new ServiceUnavailableException("financial-service");
        }
        try {
            return recurrencyClient.deleteRecurringTemplate(authorization, templateId);
            } catch (Exception e) {
            LOG.errorf("Erro ao criar transação: %s", e.getMessage());
            throw new ServiceUnavailableException("financial-service", e);
        }
    }

}
