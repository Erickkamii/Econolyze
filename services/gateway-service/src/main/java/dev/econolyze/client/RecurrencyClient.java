package dev.econolyze.client;

import dev.econolyze.dto.request.RecurringTemplateRequest;
import dev.econolyze.dto.request.UpdateRecurringRequest;
import dev.econolyze.dto.response.PagedResponse;
import dev.econolyze.dto.response.RecurringTemplateResponse;
import dev.econolyze.dto.response.RecurringTemplateSummaryResponse;
import dev.econolyze.dto.response.TransactionResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
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
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<RecurringTemplateResponse>> createRecurringTemplate(
            @HeaderParam("Authorization") String authorization,
            RecurringTemplateRequest request
    );

    @PUT
    @Path("/recurring-template/{templateId}")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<RecurringTemplateResponse>> updateRecurringTemplate(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId,
            UpdateRecurringRequest request
    );

    @GET
    @Path("/recurring-template")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<List<RecurringTemplateResponse>>> getAllRecurringTemplates(
            @HeaderParam("Authorization") String authorization
    );

    @GET
    @Path("/recurring-template/{templateId}")
    Uni<RestResponse<RecurringTemplateResponse>> getRecurringTemplateById(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId
    );

    @GET
    @Path("/recurring-template/{templateId}/history")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<PagedResponse<TransactionResponse>>> getRecurringHistory(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId
    );

    @GET
    @Path("/recurring-template/{templateId}/summary")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<RecurringTemplateSummaryResponse>> getRecurringSummary(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId
    );

    @GET
    @Path("/recurring-template/{templateId}/preview")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<PagedResponse<LocalDate>>> getRecurringPreview(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    );

    @GET
    @Path("/recurring-template/{templateId}/preview/full")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<PagedResponse<LocalDate>>> getRecurringPreviewFull(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize
    );

    @PATCH
    @Path("/recurring-template/{templateId}/toggle")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<RecurringTemplateResponse>> toggleRecurring(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId
    );

    @DELETE
    @Path("/recurring-template/{templateId}")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<Void>> deleteRecurringTemplate(
            @HeaderParam("Authorization") String authorization,
            @PathParam("templateId") Long templateId
    );

}
