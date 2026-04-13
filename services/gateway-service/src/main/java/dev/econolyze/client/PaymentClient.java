package dev.econolyze.client;

import dev.econolyze.dto.request.PaymentRequest;
import dev.econolyze.dto.response.PaymentResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
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
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<PaymentResponse>> createPayment(
            @HeaderParam("Authorization") String authorization,
            PaymentRequest paymentRequest
    );

    @GET
    @Path("/payment/{id}")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<PaymentResponse>> getPaymentById(
            @HeaderParam("Authorization") String authorization,
            @PathParam("id") Long id
    );

    @GET
    @Path("/payment/transaction/{transactionId}")
    @Timeout(5000)
    @Retry(maxRetries = 2)
    Uni<RestResponse<List<PaymentResponse>>> getPaymentsByTransactionId(
            @HeaderParam("Authorization") String authorization,
            @PathParam("transactionId") Long transactionId
    );
}
