package dev.econolyze.application.dto.response;

import dev.econolyze.domain.enums.RecurrenceFrequency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecurrencySummaryResponse(
        Long templateId,
        BigDecimal amount,
        RecurrenceFrequency frequency,
        Integer totalOccurrences,
        Integer remainingOccurrences,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        BigDecimal remainingAmount,
        LocalDate firstOccurrence,
        LocalDate lastOccurrence,
        LocalDate nextOccurrence,
        Boolean isActive
) {}