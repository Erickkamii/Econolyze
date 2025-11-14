package dev.econolyze.application.dto.request;

import dev.econolyze.domain.enums.AccountType;

import java.math.BigDecimal;

public record CreateAccountRequest(
        String name,
        AccountType type,
        BigDecimal actualBalance,
        BigDecimal creditLimit,
        Integer closingDate,
        Boolean active
) {
}
