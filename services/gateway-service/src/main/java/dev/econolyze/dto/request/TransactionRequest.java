package dev.econolyze.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        Long id,
        BigDecimal amount,
        Long userId,
        String category,
        String type,
        String description,
        LocalDate date,
        String method,
        Integer financialGoalId,
        Boolean isRecurring,
        Long recurringTemplateId
) {
}
