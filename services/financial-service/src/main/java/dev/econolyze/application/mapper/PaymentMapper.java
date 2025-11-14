package dev.econolyze.application.mapper;

import dev.econolyze.application.dto.PaymentDTO;
import dev.econolyze.application.dto.request.PaymentRequest;
import dev.econolyze.application.dto.response.PaymentResponse;
import dev.econolyze.domain.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface PaymentMapper {
    @Mapping(target = "paidAt", source = "paidAt")
    @Mapping(target = "status", source = "status")
    Payment mapToEntity(PaymentRequest request);
    @Mapping(target = "transactionId", source = "transaction.id")
    PaymentResponse mapToResponse(Payment payment);
    PaymentDTO mapToDto(Payment payment);
}
