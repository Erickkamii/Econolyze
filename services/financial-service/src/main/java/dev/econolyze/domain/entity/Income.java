package dev.econolyze.domain.entity;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.GoalType;
import dev.econolyze.domain.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "income")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal amount;
    private Long userId;
    private LocalDate date;
    @Column(name = "categoryId")
    private Integer categoryId;
    private Integer methodId;
    private Integer financialGoalId;

    @Transient
    public Category getCategory() {
        return categoryId != null ? Category.fromCode(categoryId): null;
    }

    public void setCategory(Category category) {
        this.categoryId = category.getCode();
    }

    public void setPaymentMethod(PaymentMethod method){ this.methodId = method.getCode();}
    @Transient
    public PaymentMethod getPaymentMethod(){ return methodId != null ? PaymentMethod.fromCode(methodId): null;}

    @Transient
    public GoalType getGoalType(){ return financialGoalId != null ? GoalType.fromCode(financialGoalId): null;}
    public void setGoalType(GoalType type){ this.financialGoalId = type.getCode();}
}
