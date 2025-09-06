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
    private BigDecimal amount;
    private String description;
    private Integer typeCode;
    @Column(name = "status")
    private Integer statusCode;

    @Transient
    public GoalType getType(){
        return typeCode != null ? GoalType.fromCode(typeCode): null;
    }
    public void setType(GoalType type){
        this.typeCode = type.getCode();
    }
    @Transient
    public GoalStatus getStatus(){ return statusCode != null ? GoalStatus.fromCode(statusCode): null;
    }
    public void setStatus(GoalStatus status){
        this.statusCode = status.getCode();
    }
}
