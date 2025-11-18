package dev.econolyze.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestmentProjectionResponse(
        BigDecimal amountBlank,
        BigDecimal amountCdi,
        String category,
        String description,
        LocalDate date
) {
}
