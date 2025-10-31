package dev.econolyze.application.dto;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateRecurringRequest(
        BigDecimal amount,
        Category category,
        PaymentMethod paymentMethod,
        String description,
        LocalDate endDate,
        Integer maxOccurrences
) {
}
