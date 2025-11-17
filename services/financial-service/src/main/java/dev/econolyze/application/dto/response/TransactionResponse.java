package dev.econolyze.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
        BigDecimal initialPayment,
        BigDecimal paidAmount,
        BigDecimal remainingBalance,
        String status,
        Long accountId,
        @JsonIgnoreProperties("transactionId") List<PaymentResponse> payments,
        Long recurringTemplateId
) {
}
