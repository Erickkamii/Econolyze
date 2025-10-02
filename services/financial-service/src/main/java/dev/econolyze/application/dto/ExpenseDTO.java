package dev.econolyze.application.dto;

import dev.econolyze.domain.enums.Category;
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
public class ExpenseDTO {
    private Long id;
    private String name;
    private Long userId;
    private BigDecimal amount;
    private Category category;
    private String description;
    private LocalDate date;
    private PaymentMethod method;
    private Long financialGoalId;
}
