package dev.econolyze.domain.enums;

import lombok.Getter;

@Getter
public enum TransactionType {
    INCOME( true, false),
    EXPENSE( false, true),
    TRANSFER( false, false),
    SAVINGS( false, false),
    REFUND( true, false),
    INVESTMENT( true, false);

    private final boolean increaseBalance;
    private final boolean decreaseBalance;

    TransactionType( boolean increaseBalance, boolean decreaseBalance) {
        this.increaseBalance = increaseBalance;
        this.decreaseBalance = decreaseBalance;
    }

}
