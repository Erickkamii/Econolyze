package dev.econolyze.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BalanceResponse(
        BigDecimal balance,
        LocalDate date,
        BigDecimal income,
        BigDecimal expenses,
        BigDecimal balanceDifference
) {
}
