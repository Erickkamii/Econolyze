package dev.econolyze.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateTransactionRequest(

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotBlank(message = "Category is required")
        String category,

        @NotNull(message = "Date is required")
        LocalDate date,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        Long financialGoalId
) {
    public UpdateTransactionRequest {
        if (date != null && date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Transaction date cannot be in the future");
        }
    }
}