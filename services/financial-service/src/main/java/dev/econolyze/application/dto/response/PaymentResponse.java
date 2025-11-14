package dev.econolyze.application.dto.response;

import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentResponse(
        Long transactionId,
        BigDecimal amount,
        PaymentMethod method,
        PaymentStatus status,
        LocalDate paidAt,
        String description
) {
}
