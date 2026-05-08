package com.econolyze.dev.interfaces.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestmentProjectionDTO(
        BigDecimal amountBlank,
        BigDecimal amountCdi,
        String category,
        String description,
        LocalDate date
) {
}
