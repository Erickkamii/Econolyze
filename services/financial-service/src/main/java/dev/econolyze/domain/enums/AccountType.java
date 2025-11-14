package dev.econolyze.domain.enums;

import lombok.Getter;

@Getter
public enum AccountType {
    CHECKING_ACCOUNT(1),
    SAVINGS_ACCOUNT(2),
    CREDIT_CARD(3),
    INVESTMENT_ACCOUNT(4),
    MONEY(5);
    private final int code;

    AccountType(int code) {
        this.code = code;
    }

    public static AccountType fromCode(int code) {
        for (AccountType type : AccountType.values()) {
            if (type.getCode() == code) {
                return type;
                }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
