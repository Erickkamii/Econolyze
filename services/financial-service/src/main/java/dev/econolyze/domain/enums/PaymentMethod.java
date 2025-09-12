package dev.econolyze.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum PaymentMethod {
    CREDIT_CARD_FULL(1, true),
    CASH(2, true),
    BANK_TRANSFER(3, false),
    PAYPAL(4, true),
    PIX(5, true),
    DEBIT_CARD(6, true),
    CREDIT_CARD_INSTALLMENTS(7, true);

    public final int code;
    public final boolean instantDebit;

    PaymentMethod(int code, boolean instantDebit){
        this.code = code;
        this.instantDebit = instantDebit;
    }

    public static PaymentMethod fromCode(int code){
        for(PaymentMethod p : PaymentMethod.values()){
            if(p.code == code){
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    @JsonCreator
    public static PaymentMethod fromString(String value) {
        return PaymentMethod.valueOf(value.toUpperCase());
    }
    @JsonValue
    public String toJson() {
        return this.name();
    }
}
