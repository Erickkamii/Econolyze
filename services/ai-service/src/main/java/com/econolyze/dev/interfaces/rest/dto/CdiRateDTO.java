package com.econolyze.dev.interfaces.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CdiRateDTO(
        BigDecimal currentRate,
        LocalDate lastUpdate,
        String source
) {
}
