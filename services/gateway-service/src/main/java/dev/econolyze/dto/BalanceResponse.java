package dev.econolyze.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BalanceResponse(
        @NotNull Long userId,
        BigDecimal balance,
        LocalDate date,
        BigDecimal income,
        BigDecimal expenses,
        BigDecimal balanceDifference
) {
}
