package dev.econolyze.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecurringTemplateRequest(
        BigDecimal amount,
        String type,
        String category,
        String paymentMethod,
        String description,
        String frequency,
        LocalDate startDate,
        LocalDate endDate,
        Integer maxOccurrences
) {
}
