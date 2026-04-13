package dev.econolyze.resource;

import dev.econolyze.client.PaymentClient;
import dev.econolyze.dto.request.PaymentRequest;
import dev.econolyze.dto.response.PaymentResponse;
import dev.econolyze.exception.ServiceUnavailableException;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
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

    private boolean unauthorized(){
        return jwt.getClaim("userId") == null;
    }

    @POST
    @Path("/payment")
    public Uni<RestResponse<PaymentResponse>> payment(@Context HttpHeaders headers, PaymentRequest request){
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return paymentClient.createPayment(headers.getHeaderString("Authorization"), request)
                .onFailure().invoke(e -> LOG.errorf("Erro ao criar pagamento: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service",e));
    }

    @GET
    @Path("/payment/{id}")
    public Uni<RestResponse<PaymentResponse>> getPaymentById(@Context HttpHeaders headers, @PathParam("id") Long id){
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return paymentClient.getPaymentById(headers.getHeaderString("Authorization"), id)
                .onFailure().invoke(e -> LOG.errorf("Erro ao buscar pagamento: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }

    @GET
    @Path("/payment/transaction/{transactionId}")
    public Uni<RestResponse<List<PaymentResponse>>> getPaymentByTransactionId(@Context HttpHeaders headers, @PathParam("transactionId") Long transactionId){
        if (unauthorized()) return Uni.createFrom().item(RestResponse.status(RestResponse.Status.UNAUTHORIZED));
        return paymentClient.getPaymentsByTransactionId(headers.getHeaderString("Authorization"), transactionId)
                .onFailure().invoke(e -> LOG.errorf("Erro ao buscar pagamento: %s", e.getMessage()))
                .onFailure().transform(e -> new ServiceUnavailableException("financial-service", e));
    }
}
