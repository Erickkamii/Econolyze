package dev.econolyze.client;

import dev.econolyze.dto.request.PaymentRequest;
import dev.econolyze.dto.response.PaymentResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api")
@RegisterRestClient(configKey = "financial-service")
@RegisterClientHeaders(AuthHeaderFactory.class)
public interface PaymentClient {
    @POST
    @Path("/payment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<PaymentResponse> createPayment(
            @HeaderParam("Authorization") String authorization,
            PaymentRequest paymentRequest
    );

    @GET
    @Path("/payment/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<PaymentResponse> getPaymentById(
            @HeaderParam("Authorization") String authorization,
            @PathParam("id") Long id
    );

    @GET
    @Path("/payment/transaction/{transactionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(5000)
    @Retry(maxRetries = 2)
    RestResponse<List<PaymentResponse>> getPaymentsByTransactionId(
            @HeaderParam("Authorization") String authorization,
            @PathParam("transactionId") Long transactionId
    );
}
