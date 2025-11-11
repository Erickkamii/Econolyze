package dev.econolyze.application.dto.request;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateRecurringRequest(
        Long id,
        BigDecimal amount,
        Category category,
        PaymentMethod method,
        String description,
        LocalDate endDate,
        Integer maxOccurrences
) {
}
