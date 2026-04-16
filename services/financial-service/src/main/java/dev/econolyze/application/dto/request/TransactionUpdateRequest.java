package dev.econolyze.application.dto.request;

import dev.econolyze.domain.enums.Category;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionUpdateRequest(

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Category is required")
        Category category,

        @NotNull(message = "Date is required")
        LocalDate date,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        Long financialGoalId
) {
    public TransactionUpdateRequest {
        if (date != null && date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Transaction date cannot be in the future"
            );
        }
    }
}