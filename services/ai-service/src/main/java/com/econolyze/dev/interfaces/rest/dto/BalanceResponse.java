package com.econolyze.dev.interfaces.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BalanceResponse(
        BigDecimal balance,
        LocalDate date,
        BigDecimal income,
        BigDecimal expenses,
        BigDecimal balanceDifference
) {
}
