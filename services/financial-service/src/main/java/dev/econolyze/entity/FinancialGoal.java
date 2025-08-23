package dev.econolyze.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "financial_goal")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinancialGoal {
    @Id
    private Long id;
    private String name;
    private Double amount;
    private String description;
    private String category;
    private String status;
}
