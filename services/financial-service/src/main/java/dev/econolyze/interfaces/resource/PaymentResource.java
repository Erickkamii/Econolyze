package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.request.PaymentRequest;
import dev.econolyze.application.dto.response.PaymentResponse;
import dev.econolyze.application.services.PaymentService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api/payment")
public class PaymentResource {
    @Inject
    PaymentService paymentService;

    @POST
    public Uni<RestResponse<PaymentResponse>> createPayment(PaymentRequest request) {
        return paymentService.createPayment(request).map(RestResponse::ok);
    }

    @GET
    @Path("/{id}")
    public Uni<RestResponse<PaymentResponse>> getPaymentById(@PathParam("id") Long id) {
        return paymentService.getPaymentById(id).map(RestResponse::ok);
    }

    @GET
    @Path("/transaction/{transactionId}")
    public Uni<RestResponse<List<PaymentResponse>>> getPaymentsByTransactionId(@PathParam("transactionId") Long transactionId) {
        return paymentService.getPaymentsByTransactionId(transactionId).map(RestResponse::ok);
    }
}
