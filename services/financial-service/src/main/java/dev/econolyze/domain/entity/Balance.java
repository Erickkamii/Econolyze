package dev.econolyze.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "balance")
@Getter
@Setter
@Builder
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
    @Version
    public Long version;

}
