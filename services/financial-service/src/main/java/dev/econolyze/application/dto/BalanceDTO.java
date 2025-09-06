package dev.econolyze.application.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class BalanceDTO {
    private Long userId;
    private BigDecimal balance;
    private LocalDate date;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal balanceDifference;
}
