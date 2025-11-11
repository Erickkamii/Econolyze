package dev.econolyze.application.converter;

import dev.econolyze.domain.enums.*;

public class EnumConverter {
    public static Category toCategory(int code) {
        return Category.fromCode(code);
    }
    public static int toCode(Category category) {
        return category.getCode();
    }
    public static TransactionType toTransactionType(int typeCode) {
        return TransactionType.fromCode(typeCode);
    }
    public static int toCode(TransactionType type) {
        return type.getCode();
    }
    public static PaymentMethod toPaymentMethod(int method) {
        return PaymentMethod.fromCode(method);
    }
    public static int toCode(PaymentMethod paymentMethod) {
        return paymentMethod.getCode();
    }
    public static GoalType toGoalType(int goalType){
        return GoalType.fromCode(goalType);
    }
    public static int toCode(GoalType goalType) {
        return goalType.getCode();
    }
    public static PaymentStatus toPaymentStatus(int status) {return PaymentStatus.fromCode(status);}
    public static int toCode(PaymentStatus paymentStatus) {return paymentStatus.getCode();}
    public static TransactionStatus toTransactionStatus(int status) {return TransactionStatus.fromCode(status);}
    public static int toCode(TransactionStatus transactionStatus) {return transactionStatus.getCode();}
}
