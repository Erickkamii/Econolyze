package dev.econolyze.application.dto;

import dev.econolyze.domain.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomeDTO {
    private Long id;
    private String name;
    private Long userId;
    private BigDecimal amount;
    private String category;
    private String description;
    private LocalDate date;
    private PaymentMethod method;
    private Integer financialGoalId;
}
