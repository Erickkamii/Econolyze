package com.econolyze.dev.interfaces.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionSummaryResponse(
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal income,
        BigDecimal expenses,
        BigDecimal net,
        Long transactionCount
) {
}
