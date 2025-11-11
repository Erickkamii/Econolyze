package dev.econolyze.application.dto.request;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        Long id,
        BigDecimal amount,
        BigDecimal paidAmount,
        Category category,
        TransactionType type,
        String description,
        LocalDate date,
        Boolean isRecurring,
        Long financialGoalId,
        PaymentMethod method,
        Long recurringTemplateId
) {
}
