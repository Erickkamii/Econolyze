package dev.econolyze.application.dto.request;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount,
        @NotNull(message = "Category is required")
        Category category,
        @NotNull(message = "Type is required")
        TransactionType type,
        @NotNull(message = "Date is required")
        LocalDate date,
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,
        Long accountId,
        Long financialGoalId,
        Long recurringTemplateId,
        Boolean isRecurring,

        PaymentMethod method,

        @DecimalMin(value = "0.00", message = "Initial payment cannot be negative")
        BigDecimal initialPayment
) {

    public TransactionRequest {
        if (initialPayment != null && initialPayment.compareTo(BigDecimal.ZERO) > 0) {
            if (method == null) {
                throw new IllegalArgumentException(
                        "Payment method is required when initial payment is provided"
                );
            }
            if (initialPayment.compareTo(amount) > 0) {
                throw new IllegalArgumentException(
                        "Initial payment cannot exceed transaction amount"
                );
            }
        }
        if (date != null && date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Transaction date cannot be in the future"
            );
        }
    }

    public boolean hasInitialPayment() {
        return initialPayment != null && initialPayment.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isFullyPaid() {
        return hasInitialPayment() && initialPayment.compareTo(amount) >= 0;
    }

    public boolean isPartiallyPaid() {
        return hasInitialPayment() && initialPayment.compareTo(amount) < 0;
    }
}