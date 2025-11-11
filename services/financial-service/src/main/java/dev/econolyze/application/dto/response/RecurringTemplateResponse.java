package dev.econolyze.application.dto.response;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.RecurrenceFrequency;
import dev.econolyze.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RecurringTemplateResponse(
        Long id,
        BigDecimal amount,
        TransactionType type,
        Category category,
        PaymentMethod method,
        String description,
        RecurrenceFrequency frequency,
        LocalDate startDate,
        LocalDate endDate,
        Integer maxOccurrences,
        LocalDate nextOccurrence,
        Integer timesProcessed,
        Boolean isActive,
        List<TransactionResponse> transactions
) {
}
