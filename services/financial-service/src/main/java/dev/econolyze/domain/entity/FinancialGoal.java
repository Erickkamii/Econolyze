package dev.econolyze.domain.entity;

import dev.econolyze.domain.enums.GoalStatus;
import dev.econolyze.domain.enums.GoalType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(schema = "finance", name = "financial_goal")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinancialGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private String name;
    private BigDecimal amount;
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GoalStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private GoalType type;

}
