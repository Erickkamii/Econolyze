package dev.econolyze.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialGoalDTO {
    private Long id;
    private Long userId;
    private String name;
    private BigDecimal amount;
    private String description;
    private String type;
    private String status;
}
