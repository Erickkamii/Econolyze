package dev.econolyze.dto.response;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        String name,
        String type,
        BigDecimal actualBalance,
        BigDecimal creditLimit
) {
}
