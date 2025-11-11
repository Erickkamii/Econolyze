package dev.econolyze.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RecurringTemplateResponse(
        Long id,
        BigDecimal amount,
        String type,
        String category,
        String method,
        String description,
        String frequency,
        LocalDate startDate,
        LocalDate endDate,
        Integer maxOccurrences,
        LocalDate nextOccurrence,
        Integer timesProcessed,
        Boolean isActive,
        List<TransactionResponse> transactions
) {
}
