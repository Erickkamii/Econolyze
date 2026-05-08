package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.PagedResponse;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.dto.request.TransactionRequest;
import dev.econolyze.application.dto.request.TransactionUpdateRequest;
import dev.econolyze.application.dto.response.TransactionByCategoryResponse;
import dev.econolyze.application.dto.response.TransactionResponse;
import dev.econolyze.application.dto.response.TransactionSummaryResponse;
import dev.econolyze.application.services.TransactionService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestResponse;

import java.time.LocalDate;
import java.util.List;

@Path("/api/transaction")
public class TransactionResource {
    @Inject
    TransactionService transactionService;

    @POST
    public Uni<RestResponse<TransactionResponse>> addTransaction(TransactionRequest request) {
        return transactionService.saveTransaction(request)
                .map(RestResponse::ok)
                .onFailure().recoverWithItem(RestResponse.serverError());
    }

    @PUT
    @Path("/{id}")
    public Uni<RestResponse<TransactionResponse>> updateTransaction(@PathParam("id") Long id, TransactionUpdateRequest request) {
        return transactionService.updateTransaction(id, request)
                .map(RestResponse::ok)
                .onFailure().recoverWithItem(RestResponse.serverError());
    }

    @GET
    public Uni<RestResponse<PagedResponse<TransactionResponse>>> getAllTransactionsByUserId(@QueryParam("page") @DefaultValue("0") int page,
                                                                                       @QueryParam("pageSize") @DefaultValue("20") int pageSize,
                                                                                       @QueryParam("sortBy") @DefaultValue("id") String sortBy,
                                                                                       @QueryParam("sortDir") @DefaultValue("DESC") String sortDir,
                                                                                       @QueryParam("type") String type,
                                                                                       @QueryParam("category") String category) {
        return transactionService.getAllTransactionsByUserId(page, pageSize, sortBy, sortDir, type, category)
                .map(RestResponse::ok)
                .onFailure().recoverWithItem(RestResponse.serverError());
    }

    @GET
    @Path("/{id}")
    public Uni<RestResponse<TransactionResponse>> getTransactionById(@PathParam("id") Long id) {
        return transactionService.getTransactionById(id).map(RestResponse::ok)
                .onFailure().recoverWithItem(RestResponse.notFound());
    }

    @GET
    @Path("/internal/rag")
    public Uni<RestResponse<List<TransactionDTO>>> getTransactionsByUserIdAndDate(
            @QueryParam("date") String date
    ) {
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date);
        } catch (Exception e) {
            return Uni.createFrom().item(
                    RestResponse.status(RestResponse.Status.BAD_REQUEST)
            );
        }
        return transactionService.getAllTransactionsInternal(parsedDate)
                .map(RestResponse::ok)
                .onFailure().recoverWithItem(e -> {
                    e.printStackTrace();
                    return RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR);
                });
    }

    @GET
    @Path("/summary")
    public Uni<RestResponse<TransactionSummaryResponse>> getTransactionSummary(
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate
    ){
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return transactionService.getFinancialSummary(start, end)
                .map(RestResponse::ok)
                .onFailure().recoverWithItem(RestResponse.serverError());
    }

    @GET
    @Path("/categories")
    public Uni<RestResponse<List<TransactionByCategoryResponse>>> getByType(
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("type") String type
    ) {
        LocalDate start;
        LocalDate end;

        try {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
        } catch (Exception e) {
            return Uni.createFrom().item(RestResponse.status(RestResponse.Status.BAD_REQUEST));
        }

        return transactionService.getByType(start, end, type)
                .map(RestResponse::ok)
                .onFailure(BadRequestException.class)
                .recoverWithItem(RestResponse.status(RestResponse.Status.BAD_REQUEST))
                .onFailure().invoke(Throwable::printStackTrace)
                .onFailure()
                .recoverWithItem(RestResponse.serverError());
    }
}
