package dev.econolyze.dto;

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
    private BigDecimal amount;
    private String category;
    private String description;
    private LocalDate date;
}
