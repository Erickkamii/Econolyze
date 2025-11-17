package dev.econolyze.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount,
        @NotNull(message = "Category is required")
        String category,
        @NotNull(message = "Type is required")
        String type,
        @NotNull(message = "Date is required")
        LocalDate date,
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,
        Long accountId,
        Long financialGoalId,
        Long recurringTemplateId,
        Boolean isRecurring,

        String method,

        @DecimalMin(value = "0.00", message = "Initial payment cannot be negative")
        BigDecimal initialPayment
) {
}