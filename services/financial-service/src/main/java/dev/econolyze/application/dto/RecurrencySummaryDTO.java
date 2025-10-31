package dev.econolyze.application.dto;

import dev.econolyze.domain.enums.RecurrenceFrequency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecurrencySummaryDTO(
        Long templateId,
        BigDecimal amount,
        RecurrenceFrequency frequency,
        Integer totalOccurrences,      // total de parcelas
        Integer remainingOccurrences,  // parcelas restantes
        BigDecimal totalAmount,        // valor total (amount × totalOccurrences)
        BigDecimal paidAmount,         // já pago
        BigDecimal remainingAmount,    // falta pagar
        LocalDate firstOccurrence,
        LocalDate lastOccurrence,
        LocalDate nextOccurrence,
        Boolean isActive
) {}