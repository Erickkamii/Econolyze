package dev.econolyze.dto.request;

import java.math.BigDecimal;

public record AccountRequest(
        String name,
        String type,
        BigDecimal actualBalance,
        BigDecimal creditLimit,
        Integer closingDate,
        Boolean active
) {
}
