package dev.econolyze.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        String category,
        String type,
        String description,
        LocalDate date,
        String method,
        Integer financialGoalId,
        Boolean isRecurring,
        Long recurringTemplateId
) {}
