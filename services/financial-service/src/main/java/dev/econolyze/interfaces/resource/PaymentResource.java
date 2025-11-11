package dev.econolyze.interfaces.resource;

import dev.econolyze.application.dto.request.PaymentRequest;
import dev.econolyze.application.dto.response.PaymentResponse;
import dev.econolyze.application.services.PaymentService;
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
    public RestResponse<PaymentResponse> createPayment(PaymentRequest request) {
        PaymentResponse created = paymentService.createPayment(request);
        return RestResponse.ok(created);
    }

    @GET
    @Path("/{id}")
    public RestResponse<PaymentResponse> getPaymentById(@PathParam("id") Long id) {
        PaymentResponse payment = paymentService.getPaymentById(id);
        return RestResponse.ok(payment);
    }

    @GET
    @Path("/transaction/{transactionId}")
    public RestResponse<List<PaymentResponse>> getPaymentsByTransactionId(@PathParam("transactionId") Long transactionId) {
        var payments = paymentService.getPaymentsByTransactionId(transactionId);
        return RestResponse.ok(payments);
    }
}
