package dev.econolyze.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecurringTemplateSummaryResponse(
        Long templateId,
        BigDecimal amount,
        String frequency,
        Integer totalOccurrences,
        Integer remainingOccurrences,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        BigDecimal remainingAmount,
        LocalDate firstOccurrence,
        LocalDate lastOccurrence,
        LocalDate nextOccurrence,
        Boolean isActive
) {
}
