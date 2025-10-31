package dev.econolyze.domain.entity;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.GoalType;
import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "amount")
    private BigDecimal amount;
    private Long userId;
    @Column(name = "financialGoalId")
    private Integer financialGoalId;
    @Column(name = "category")
    private Category category;
    @Column(name = "type")
    private TransactionType type;
    @Column(name = "method_code")
    private PaymentMethod method;
    @Column(name = "recurring_template_id")
    private Long recurringTemplateId;
    @Column(name = "is_recurring")
    private Boolean isRecurring;
    private String description;
    private LocalDate date;

    @Transient
    public GoalType getGoalType(){ return financialGoalId != null ? GoalType.fromCode(financialGoalId): null;}
    public void setGoalType(GoalType type){ this.financialGoalId = type.getCode();}
}
