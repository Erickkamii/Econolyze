package dev.econolyze.application.dto;

import dev.econolyze.domain.enums.GoalStatus;
import dev.econolyze.domain.enums.GoalType;
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
    private GoalType type;
    private GoalStatus status;
}
