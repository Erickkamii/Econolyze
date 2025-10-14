package dev.econolyze.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CdiRateResponse(
    BigDecimal currentRate,
    LocalDate lastUpdate,
    String source
) {}

