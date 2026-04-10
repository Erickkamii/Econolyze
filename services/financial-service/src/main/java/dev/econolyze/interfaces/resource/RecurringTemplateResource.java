package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.*;
import dev.econolyze.application.dto.request.CreateRecurringRequest;
import dev.econolyze.application.dto.request.UpdateRecurringRequest;
import dev.econolyze.application.dto.response.RecurrencySummaryResponse;
import dev.econolyze.application.dto.response.RecurringTemplateResponse;
import dev.econolyze.application.dto.response.TransactionResponse;
import dev.econolyze.application.services.RecurringTransactionService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;

import java.time.LocalDate;
import java.util.List;

@Path("/api/recurring-template")
@RequiredArgsConstructor
public class RecurringTemplateResource {
    @Inject
    RecurringTransactionService recurringTransactionService;

    @POST
    public Uni<RestResponse<RecurringTemplateResponse>> createRecurrency(CreateRecurringRequest request) {
        return recurringTransactionService.createRecurring(request).map(RestResponse::ok);
    }

    @PUT
    @Path("/{id}")
    public Uni<RestResponse<RecurringTemplateResponse>> update(
            @PathParam("id") Long templateId,
            UpdateRecurringRequest request) {
        return recurringTransactionService.updateTemplate(templateId, request).map(RestResponse::ok);
    }

    @GET
    public Uni<RestResponse<List<RecurringTemplateResponse>>> listActiveRecurrencyTemplates() {
        return recurringTransactionService.getAllTemplatesByUserId().map(RestResponse::ok);
    }

    @GET
    @Path("/{id}")
    public Uni<RestResponse<RecurringTemplateResponse>> getById(@PathParam("id") Long templateId) {
        return recurringTransactionService.getTemplateById(templateId).map(RestResponse::ok);
    }

    @GET
    @Path("/{id}/history")
    public Uni<RestResponse<PagedResponse<TransactionResponse>>> getHistory(
            @PathParam("id") Long templateId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize) {
        return recurringTransactionService.getTransactionHistory(templateId, page, pageSize).map(RestResponse::ok);
    }

    @GET
    @Path("/{templateId}/preview")
    public Uni<RestResponse<PagedResponse<LocalDate>>> previewSimple(@PathParam("templateId") Long templateId,
                                                                @QueryParam("page") @DefaultValue("0") int page,
                                                                @QueryParam("pageSize") @DefaultValue("20") int pageSize,
                                                                @QueryParam("maxResults")@DefaultValue("30")  Integer maxResults) {
        return recurringTransactionService.previewNextRecurrencies(templateId, page, pageSize,maxResults).map(RestResponse::ok);
    }

    @GET
    @Path("/{templateId}/preview/full")
    public Uni<RestResponse<PagedResponse<LocalDate>>> previewPaginated(
            @PathParam("templateId") Long templateId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("12") int pageSize,
            @QueryParam("maxResults") @DefaultValue("1000") Integer maxResults) {
        return recurringTransactionService.previewNextRecurrencies(templateId, page, pageSize, maxResults).map(RestResponse::ok);
    }

    @GET
    @Path("/{id}/summary")
    public Uni<RestResponse<RecurrencySummaryResponse>> getSummary(@PathParam("id") Long templateId) {
        return recurringTransactionService.getRecurrencySummary(templateId).map(RestResponse::ok);
    }

    @PATCH
    @Path("/{id}/toggle")
    public Uni<RestResponse<RecurringTemplateResponse>> toggleActive(@PathParam("id") Long templateId) {
        return recurringTransactionService.toggleActive(templateId).map(RestResponse::ok);
    }

    @DELETE
    @Path("/{id}")
    public Uni<RestResponse<Void>> delete(@PathParam("id") Long templateId) {
        return recurringTransactionService.deleteTemplate(templateId).replaceWith(RestResponse.noContent());
    }

}
