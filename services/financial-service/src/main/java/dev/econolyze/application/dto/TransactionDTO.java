package dev.econolyze.application.dto;

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
    private String category;
    private String type;
    private String description;
    private LocalDate date;
    private String method;
    private Long financialGoalId;

}
