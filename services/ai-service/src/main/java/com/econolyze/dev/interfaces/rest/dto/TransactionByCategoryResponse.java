package com.econolyze.dev.interfaces.rest.dto;

import java.math.BigDecimal;

public record TransactionByCategoryResponse(
        String type,
        String category,
        BigDecimal amount,
        BigDecimal percentage
) {
}
