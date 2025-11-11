package dev.econolyze.domain.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    PAID(1),
    PAID_PARTIALLY(2),
    PENDING(3),
    CANCELLED(4);

    private final int code;

    TransactionStatus(int code){ this.code = code;}

    public static TransactionStatus fromCode(int code) {
        for (TransactionStatus t: TransactionStatus.values()){
            if (t.getCode()==code){
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
