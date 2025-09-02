package dev.econolyze.entity;

import dev.econolyze.enums.GoalType;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private String name;
    private Double amount;
    private String description;
    private Integer typeCode;
    private String status;

    @Transient
    public GoalType getCategory(){
        return typeCode != null ? GoalType.fromCode(typeCode): null;
    }
    public void setType(GoalType type){
        this.typeCode = type.getCode();
    }
}
