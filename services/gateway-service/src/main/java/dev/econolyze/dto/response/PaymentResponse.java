package dev.econolyze.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentResponse(
        Long transactionId,
        BigDecimal amount,
        String method,
        String status,
        LocalDate paidAt,
        String description
) {
}
