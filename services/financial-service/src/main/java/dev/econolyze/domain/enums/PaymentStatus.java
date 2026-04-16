package dev.econolyze.domain.enums;


import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REFUNDED
}
