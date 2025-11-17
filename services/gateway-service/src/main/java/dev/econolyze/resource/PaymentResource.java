package dev.econolyze.resource;

import dev.econolyze.client.PaymentClient;
import dev.econolyze.dto.request.PaymentRequest;
import dev.econolyze.dto.response.PaymentResponse;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;

import org.jboss.logging.Logger;

import java.util.List;


@Path("/api/financial")
@Authenticated
public class PaymentResource {
    private static final Logger LOG = Logger.getLogger(PaymentResource.class);

    @Inject
    @RestClient
    PaymentClient paymentClient;
    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/payment")
    public RestResponse<PaymentResponse> payment(@Context HttpHeaders headers, PaymentRequest request){
        if (jwt.getClaim("userId") == null){
            LOG.warn("JWT inválido: claim userId ausente");
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return paymentClient.createPayment(authorization, request);
        } catch (Exception e) {
            LOG.errorf("Erro ao criar pagamento: %s", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/payment/{id}")
    public RestResponse<PaymentResponse> getPaymentById(@Context HttpHeaders headers, @PathParam("id") Long id){
        if (jwt.getClaim("userId") == null){
            LOG.warn("JWT inválido: claim userId ausente");
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return paymentClient.getPaymentById(authorization, id);
        } catch (Exception e) {
            LOG.errorf("Erro ao buscar pagamento: %s", e.getMessage());
            throw new RuntimeException(e);

        }
    }

    @GET
    @Path("/payment/transaction/{transactionId}")
    public RestResponse<List<PaymentResponse>> getPaymentByTransactionId(@Context HttpHeaders headers, @PathParam("transactionId") Long transactionId){
        if (jwt.getClaim("userId") == null){
            LOG.warn("JWT inválido: claim userId ausente");
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED);
        }
        String authorization = headers.getHeaderString("Authorization");
        try {
            return paymentClient.getPaymentsByTransactionId(authorization, transactionId);
        } catch (Exception e) {
            LOG.errorf("Erro ao buscar pagamento: %s", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
