package dev.econolyze.domain.enums;


import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING(1),
    COMPLETED(2),
    FAILED(3),
    REFUNDED(4);

    private final int code;

    PaymentStatus(int code) {
        this.code = code;
    }

    public static PaymentStatus fromCode(int code) {
        for (PaymentStatus p : PaymentStatus.values()) {
            if (p.getCode() == code) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
