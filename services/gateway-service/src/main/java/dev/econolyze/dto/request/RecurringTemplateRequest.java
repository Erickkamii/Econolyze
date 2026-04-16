package dev.econolyze.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RecurringTemplateRequest(
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be positive")
        BigDecimal amount,
        @NotBlank(message = "Type is required")
        String type,
        @NotBlank(message = "Category is required")
        String category,
        String paymentMethod,
        String description,
        @NotBlank(message = "Frequency is required")
        String frequency,
        @NotNull(message = "Start date is required")
        LocalDate startDate,
        LocalDate endDate,
        Integer maxOccurrences
) {
}