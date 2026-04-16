package dev.econolyze.domain.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    PAID,
    PAID_PARTIALLY,
    PENDING,
    CANCELLED
}
