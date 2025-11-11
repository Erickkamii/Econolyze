package dev.econolyze.application.dto.request;

import dev.econolyze.domain.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        Long transactionId,
        BigDecimal amount,
        PaymentMethod method,
        String description
) {
}
