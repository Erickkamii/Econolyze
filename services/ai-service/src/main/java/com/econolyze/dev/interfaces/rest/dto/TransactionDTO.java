package com.econolyze.dev.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record TransactionDTO(
        Long id,
        BigDecimal amount,
        Long userId,
        Long financialGoalId,
        Long accountId,
        String category,
        String type,
        String method,
        Long recurringTemplateId,
        Boolean isRecurring,
        String status,
        String description,
        LocalDate date,
        @JsonIgnoreProperties("transactionId") List<PaymentDTO> payments
) {
}
