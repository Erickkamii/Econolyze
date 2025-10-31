package dev.econolyze.application.dto;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.RecurrenceFrequency;
import dev.econolyze.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateRecurringRequest(
        BigDecimal amount,
        TransactionType type,
        Category category,
        PaymentMethod paymentMethod,
        String description,
        RecurrenceFrequency frequency,
        LocalDate startDate,
        LocalDate endDate,
        Integer maxOccurrences
) {
    public CreateRecurringRequest {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount is required and must be positive");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type is required");
        }
        if (frequency == null) {
            throw new IllegalArgumentException("Frequency is required");
        }
    }
}