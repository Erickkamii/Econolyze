package dev.econolyze.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentRequest(
        Long transactionId,
        BigDecimal amount,
        String method,
        LocalDate paidAt,
        String status,
        Long accountId
) {
}
