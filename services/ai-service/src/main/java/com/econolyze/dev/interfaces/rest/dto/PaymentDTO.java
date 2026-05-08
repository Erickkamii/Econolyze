package com.econolyze.dev.interfaces.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentDTO(
        Long id,
        Long transactionId,
        BigDecimal amount,
        String method,
        String status,
        LocalDate paidAt,
        Long accountId,
        Long userId
) {
}
