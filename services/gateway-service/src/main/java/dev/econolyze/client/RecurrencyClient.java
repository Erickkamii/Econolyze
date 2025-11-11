package dev.econolyze.client;

import dev.econolyze.dto.request.RecurringTemplateRequest;
import dev.econolyze.dto.request.UpdateRecurringRequest;
import dev.econolyze.dto.response.PagedResponse;
import dev.econolyze.dto.response.RecurringTemplateResponse;
import dev.econolyze.dto.response.RecurringTemplateSummaryResponse;
import dev.econolyze.dto.response.TransactionResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

import java.time.LocalDate;
import java.util.List;

@Path("/api")
@RegisterRestClient(configKey = "financial-service")
@RegisterClientHeaders(AuthHeaderFactory.class)
public interface RecurrencyClient {

    @POST
    @Path("/recurring-template")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<RecurringTemplateResponse> createRecurringTemplate(
            @HeaderParam("Authorization") String authorization,
            RecurringTemplateRequest request
    );

    @PUT
    @Path("/recurring-template/{templateId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<RecurringTemplateResponse> updateRecurringTemplate(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId,
            UpdateRecurringRequest request
    );

    @GET
    @Path("/recurring-template")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<List<RecurringTemplateResponse>> getAllRecurringTemplates(
            @HeaderParam("Authorization") String authorization
    );

    @GET
    @Path("/recurring-template/{templateId}")
    @Produces(MediaType.APPLICATION_JSON)
    RestResponse<RecurringTemplateResponse> getRecurringTemplateById(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId
    );

    @GET
    @Path("/recurring-template/{templateId}/history")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<PagedResponse<TransactionResponse>> getRecurringHistory(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId
    );

    @GET
    @Path("/recurring-template/{templateId}/summary")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<RecurringTemplateSummaryResponse> getRecurringSummary(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId
    );

    @GET
    @Path("/recurring-template/{templateId}/preview")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<PagedResponse<LocalDate>> getRecurringPreview(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    );

    @GET
    @Path("/recurring-template/{templateId}/preview/full")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<PagedResponse<LocalDate>> getRecurringPreviewFull(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    );

    @PATCH
    @Path("/recurring-template/{templateId}/toggle")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<RecurringTemplateResponse> toggleRecurring(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId
    );

    @DELETE
    @Path("/recurring-template/{templateId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<Void> deleteRecurringTemplate(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId
    );

}
