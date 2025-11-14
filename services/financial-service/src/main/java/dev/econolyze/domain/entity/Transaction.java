package dev.econolyze.domain.entity;

import dev.econolyze.domain.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transactions")
@Builder
@Getter
@Setter
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
    private Long financialGoalId;
    @Column(name = "account_id")
    private Long accountId;
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
    private TransactionStatus status = TransactionStatus.PENDING;
    private String description;
    private LocalDate date;
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();



    @Transient
    private BigDecimal cachedTotalPaid;

    public BigDecimal getPaidAmount() {
        if (cachedTotalPaid != null) return cachedTotalPaid;

        if (payments == null || payments.isEmpty()) {
            cachedTotalPaid = BigDecimal.ZERO;
            return cachedTotalPaid;
        }

        cachedTotalPaid = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return cachedTotalPaid;
    }

    public BigDecimal getRemainingBalance() {
        return amount.subtract(getPaidAmount());
    }

    public void recalculateStatus() {

        if (status == TransactionStatus.CANCELLED)
            return;


        BigDecimal totalPaid = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
            status = TransactionStatus.PENDING;
        } else if (totalPaid.compareTo(amount) >= 0) {
            status = TransactionStatus.PAID;
        } else {
            status = TransactionStatus.PAID_PARTIALLY;
        }
    }


    @PrePersist
    @PreUpdate
    public void onSave(){
        if (status == null)
            status = TransactionStatus.PENDING;
    }
}
