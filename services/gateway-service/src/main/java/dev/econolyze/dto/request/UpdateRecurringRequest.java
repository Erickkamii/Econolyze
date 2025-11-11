package dev.econolyze.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateRecurringRequest(
        Long id,
        BigDecimal amount,
        String category,
        String method,
        String description,
        LocalDate endDate,
        Integer maxOccurrences
) {
}
