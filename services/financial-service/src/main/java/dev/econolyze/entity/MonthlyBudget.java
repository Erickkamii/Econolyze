package dev.econolyze.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "monthly_budget")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyBudget {
    @Id
    private Long id;
    private Double amount;
    private Long userId;
}
