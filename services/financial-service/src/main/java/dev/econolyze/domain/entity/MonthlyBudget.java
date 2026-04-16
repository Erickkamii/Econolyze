package dev.econolyze.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(schema = "finance", name = "monthly_budget")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyBudget {
    @Id
    private Long id;
    private BigDecimal amount;
    @Column(name = "user_id")
    private Long userId;
}
