package dev.econolyze.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "balance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Balance {
    @Id
    @Column(name = "user_id")
    private Long userId;
    private BigDecimal balance;
    private LocalDate date;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal balanceDifference;
    @Version
    public Long version;

}
