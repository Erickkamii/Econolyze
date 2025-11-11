package dev.econolyze.domain.entity;


import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
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
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;
    private BigDecimal amount;
    private PaymentMethod method;
    @Column(name = "paid_at")
    private LocalDate paidAt = LocalDate.now();
    private PaymentStatus status = PaymentStatus.COMPLETED;
    private String description;

    @PrePersist
    @PreUpdate
    public void updateTransaction(){
        if (transaction != null){
            transaction.recalculateStatus();
        }
    }
}
