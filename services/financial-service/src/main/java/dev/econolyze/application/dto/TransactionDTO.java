package dev.econolyze.application.dto;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.TransactionStatus;
import dev.econolyze.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private Long userId;
    private Long financialGoalId;
    private Long accountId;
    private Category category;
    private TransactionType type;
    private PaymentMethod method;
    private Long recurringTemplateId;
    private Boolean isRecurring;
    private TransactionStatus status;
    private String description;
    private LocalDate date;
    private List<PaymentDTO> payments = new ArrayList<>();
}
