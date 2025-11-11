package dev.econolyze.application.mapper;

import dev.econolyze.application.dto.request.PaymentRequest;
import dev.econolyze.application.dto.response.PaymentResponse;
import dev.econolyze.domain.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface PaymentMapper {
    Payment mapToEntity(PaymentRequest request);
    @Mapping(target = "transactionId", source = "transaction.id")
    @Mapping(target = "transactionStatus", source = "transaction.status")
    PaymentResponse mapToResponse(Payment payment);
}
