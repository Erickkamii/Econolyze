package dev.econolyze.application.dto;

import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long id;
    private Long transactionId;
    private BigDecimal amount;
    private PaymentMethod method;
    private LocalDate paidAt;
    private PaymentStatus status;
    private Long accountId;
    private Long userId;
}
