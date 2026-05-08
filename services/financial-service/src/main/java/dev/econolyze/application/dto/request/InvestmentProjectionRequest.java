package dev.econolyze.application.dto.request;

import java.math.BigDecimal;

public record InvestmentProjectionRequest(
        BigDecimal amount,
        Integer months
) {
}
