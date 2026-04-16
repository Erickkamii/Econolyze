package dev.econolyze.domain.entity;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.RecurrenceFrequency;
import dev.econolyze.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(schema = "finance", name = "recurrency_template")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecurringTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;
    private String description;
    @Enumerated(EnumType.STRING)
    private RecurrenceFrequency frequency;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "max_occurrences")
    private Integer maxOccurrences;
    @Column(name = "next_occurrence")
    private LocalDate nextOccurrence;
    @Column(name = "times_processed")
    private Integer timesProcessed;
    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "recurringTemplateId")
    private List<Transaction> transactions;
}
