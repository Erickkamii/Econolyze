package dev.econolyze.dto.request;

import java.math.BigDecimal;

public record UpdateAccountRequest(
        Long id,
        String name,
        String type,
        BigDecimal actualBalance,
        BigDecimal creditLimit,
        Integer closingDate,
        Boolean active
) {
}
