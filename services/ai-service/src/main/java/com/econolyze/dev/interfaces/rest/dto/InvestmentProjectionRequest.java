package com.econolyze.dev.interfaces.rest.dto;

import java.math.BigDecimal;

public record InvestmentProjectionRequest(
        BigDecimal amount,
        Integer months
) {
}
