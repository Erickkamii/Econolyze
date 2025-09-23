package dev.econolyze.domain.entity;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.GoalType;
import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private Long userId;
    @Column(name = "financialGoalId")
    private Integer financialGoalId;
    @Column(name = "category_code")
    private Integer categoryCode;
    @Column(name = "type_code")
    private Integer typeCode;
    @Column(name = "method_code")
    private Integer methodCode;
    private String description;
    private LocalDate date;

    @Transient
    public TransactionType getType() {
        return typeCode != null ? TransactionType.fromCode(typeCode): null;
    }

    public void setType(TransactionType type) {
        this.typeCode = type.getCode();
    }

    @Transient
    public Category getCategory() {
        return categoryCode != null ? Category.fromCode(categoryCode): null;
    }

    public void setCategory(Category category) {
        this.categoryCode = category.getCode();
    }

    @Transient
    public PaymentMethod getMethod() {
        return methodCode != null ? PaymentMethod.fromCode(methodCode): null;
    }

    public void setMethod(PaymentMethod paymentMethod) { this.methodCode = paymentMethod.getCode();}

    @Transient
    public GoalType getGoalType(){ return financialGoalId != null ? GoalType.fromCode(financialGoalId): null;}
    public void setGoalType(GoalType type){ this.financialGoalId = type.getCode();}
}
