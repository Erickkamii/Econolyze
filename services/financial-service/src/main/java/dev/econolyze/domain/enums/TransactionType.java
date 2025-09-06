package dev.econolyze.domain.enums;

import lombok.Getter;

@Getter
public enum TransactionType {
    INCOME(1, true, false),
    EXPENSE(2, false, true),
    TRANSFER(3, false, false),
    SAVINGS(4, false, false),
    INVESTMENT(5, true, false),
    REFUND(6, true, false);

    private final int code;
    private final boolean increaseBalance;
    private final boolean decreaseBalance;

    TransactionType(int code, boolean increaseBalance, boolean decreaseBalance) {
        this.code = code;
        this.increaseBalance = increaseBalance;
        this.decreaseBalance = decreaseBalance;
    }

    public static TransactionType fromCode(int code) {
        for (TransactionType t : TransactionType.values()) {
            if (t.getCode() == code) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
