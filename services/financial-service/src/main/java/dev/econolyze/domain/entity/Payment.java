package dev.econolyze.domain.entity;


import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(schema = "finance", name = "payments")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", foreignKey = @ForeignKey(name = "fk_payments_transaction_id"))
    private Transaction transaction;
    @Column(name = "account_id")
    private Long accountId;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;
    @Column(name = "paid_at")
    private LocalDate paidAt = LocalDate.now();
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.COMPLETED;
    private String description;

    @PrePersist
    @PreUpdate
    public void updateTransaction(){
        if (transaction != null){
            transaction.recalculateStatus();
        }
        if(paidAt == null){
            paidAt = LocalDate.now();
        }
        if(status == null){
            status = PaymentStatus.COMPLETED;
        }
    }
}
