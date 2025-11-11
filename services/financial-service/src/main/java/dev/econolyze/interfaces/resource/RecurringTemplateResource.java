package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.*;
import dev.econolyze.application.dto.request.CreateRecurringRequest;
import dev.econolyze.application.dto.request.UpdateRecurringRequest;
import dev.econolyze.application.dto.response.RecurrencySummaryResponse;
import dev.econolyze.application.dto.response.RecurringTemplateResponse;
import dev.econolyze.application.dto.response.TransactionResponse;
import dev.econolyze.application.services.RecurringTransactionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestResponse;

import java.time.LocalDate;
import java.util.List;

@Path("/api/recurring-template")
public class RecurringTemplateResource {
    @Inject
    RecurringTransactionService recurringTransactionService;

    @POST
    public RestResponse<RecurringTemplateResponse> createRecurrency(CreateRecurringRequest request) {
        RecurringTemplateResponse created = recurringTransactionService.createRecurring(request);
        return RestResponse.ok(created);
    }

    @PUT
    @Path("/{id}")
    public RestResponse<RecurringTemplateResponse> update(
            @PathParam("id") Long templateId,
            UpdateRecurringRequest request) {

        RecurringTemplateResponse updated = recurringTransactionService.updateTemplate(templateId, request);
        return RestResponse.ok(updated);
    }

    @GET
    public RestResponse<List<RecurringTemplateResponse>> listActiveRecurrencyTemplates() {
        List<RecurringTemplateResponse> templates = recurringTransactionService.getAllTemplatesByUserId();
        return RestResponse.ok(templates);
    }

    @GET
    @Path("/{id}")
    public RestResponse<RecurringTemplateResponse> getById(@PathParam("id") Long templateId) {
        RecurringTemplateResponse template = recurringTransactionService.getTemplateById(templateId);
        return RestResponse.ok(template);
    }

    @GET
    @Path("/{id}/history")
    public RestResponse<PagedResponse<TransactionResponse>> getHistory(
            @PathParam("id") Long templateId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize) {

        PagedResponse<TransactionResponse> history = recurringTransactionService.getTransactionHistory(
                templateId,
                page,
                pageSize
        );
        return RestResponse.ok(history);
    }

    @GET
    @Path("/{templateId}/preview")
    public RestResponse<PagedResponse<LocalDate>> previewSimple(@PathParam("templateId") Long templateId,
                                                                @QueryParam("page") @DefaultValue("0") int page,
                                                                @QueryParam("pageSize") @DefaultValue("20") int pageSize,
                                                                @QueryParam("maxResults")@DefaultValue("30")  Integer maxResults) {
        PagedResponse<LocalDate> preview = recurringTransactionService.previewNextRecurrencies(
                templateId,
                page, pageSize,maxResults
        );
        return RestResponse.ok(preview);
    }

    @GET
    @Path("/{templateId}/preview/full")
    public RestResponse<PagedResponse<LocalDate>> previewPaginated(
            @PathParam("templateId") Long templateId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("12") int pageSize,
            @QueryParam("maxResults") @DefaultValue("1000") Integer maxResults) {

        PagedResponse<LocalDate> preview = recurringTransactionService.previewNextRecurrencies(
                templateId,
                page,
                pageSize,
                maxResults
        );
        return RestResponse.ok(preview);
    }

    @GET
    @Path("/{id}/summary")
    public RestResponse<RecurrencySummaryResponse> getSummary(@PathParam("id") Long templateId) {
        RecurrencySummaryResponse summary = recurringTransactionService.getRecurrencySummary(templateId);
        return RestResponse.ok(summary);
    }

    @PATCH
    @Path("/{id}/toggle")
    public RestResponse<RecurringTemplateResponse> toggleActive(@PathParam("id") Long templateId) {
        RecurringTemplateResponse updated = recurringTransactionService.toggleActive(templateId);
        return RestResponse.ok(updated);
    }

    @DELETE
    @Path("/{id}")
    public RestResponse<Void> delete(@PathParam("id") Long templateId) {
        recurringTransactionService.deleteTemplate(templateId);
        return RestResponse.noContent();
    }

}
