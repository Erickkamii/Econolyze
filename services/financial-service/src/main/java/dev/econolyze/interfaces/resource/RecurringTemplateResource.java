package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.*;
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
    public RestResponse<RecurrencyTemplateDTO> createRecurrency(CreateRecurringRequest request) {
        RecurrencyTemplateDTO created = recurringTransactionService.createRecurring(request);
        return RestResponse.ok(created);
    }

    @GET
    public RestResponse<List<RecurrencyTemplateDTO>> listActiveRecurrencyTemplates(@HeaderParam("X-User-Id") Long userId) {
        List<RecurrencyTemplateDTO> templates = recurringTransactionService.getAllTemplatesByUserId(userId);
        return RestResponse.ok(templates);
    }

    @GET
    @Path("/{id}")
    public RestResponse<RecurrencyTemplateDTO> getById(@PathParam("id") Long templateId) {
        RecurrencyTemplateDTO template = recurringTransactionService.getTemplateById(templateId);
        return RestResponse.ok(template);
    }

    @PUT
    @Path("/{id}")
    public RestResponse<RecurrencyTemplateDTO> update(
            @PathParam("id") Long templateId,
            UpdateRecurringRequest request) {

        RecurrencyTemplateDTO updated = recurringTransactionService.updateTemplate(templateId, request);
        return RestResponse.ok(updated);
    }

    @DELETE
    @Path("/{id}")
    public RestResponse<Void> delete(@PathParam("id") Long templateId) {
        recurringTransactionService.deleteTemplate(templateId);
        return RestResponse.noContent();
    }

    @PATCH
    @Path("/{id}/toggle")
    public RestResponse<RecurrencyTemplateDTO> toggleActive(@PathParam("id") Long templateId) {
        RecurrencyTemplateDTO updated = recurringTransactionService.toggleActive(templateId);
        return RestResponse.ok(updated);
    }

    @GET
    @Path("/{id}/history")
    public RestResponse<PagedResponse<TransactionDTO>> getHistory(
            @PathParam("id") Long templateId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize) {

        PagedResponse<TransactionDTO> history = recurringTransactionService.getTransactionHistory(
                templateId,
                page,
                pageSize
        );
        return RestResponse.ok(history);
    }

    @GET
    @Path("/{id}/preview")
    public RestResponse<PagedResponse<LocalDate>> previewSimple(@PathParam("id") Long templateId,
                                                                @QueryParam("page") @DefaultValue("0") int page,
                                                                @QueryParam("pageSize") @DefaultValue("20") int pageSize,
                                                                @QueryParam("maxResults")@DefaultValue("100")  Integer maxResults) {
        PagedResponse<LocalDate> preview = recurringTransactionService.previewNextRecurrencies(
                templateId,
                page, pageSize,maxResults
        );
        return RestResponse.ok(preview);
    }

    @GET
    @Path("/{id}/preview/full")
    public RestResponse<PagedResponse<LocalDate>> previewPaginated(
            @PathParam("id") Long templateId,
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
    public RestResponse<RecurrencySummaryDTO> getSummary(@PathParam("id") Long templateId) {
        RecurrencySummaryDTO summary = recurringTransactionService.getRecurrencySummary(templateId);
        return RestResponse.ok(summary);
    }
}
