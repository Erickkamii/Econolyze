package dev.econolyze.domain.entity;

import dev.econolyze.domain.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(schema = "finance", name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private String name;
    @Enumerated(EnumType.STRING)
    private AccountType type;
    @Column(name = "actual_balance")
    private BigDecimal actualBalance;
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;
    @Column(name = "closing_date")
    private Integer closingDate;
    private Boolean active;
}
