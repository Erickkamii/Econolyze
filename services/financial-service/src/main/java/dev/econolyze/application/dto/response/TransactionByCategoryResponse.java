package dev.econolyze.application.dto.response;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.TransactionType;

import java.math.BigDecimal;

public record TransactionByCategoryResponse(
        TransactionType type,
        Category category,
        BigDecimal amount,
        BigDecimal percentage
) {
}
