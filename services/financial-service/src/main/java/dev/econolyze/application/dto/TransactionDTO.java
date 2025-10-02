package dev.econolyze.application.dto;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.TransactionType;
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
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private Long userId;
    private Category category;
    private TransactionType type;
    private String description;
    private LocalDate date;
    private PaymentMethod method;
    private Integer financialGoalId;

}
