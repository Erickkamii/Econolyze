package dev.econolyze.application.converter;

import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.GoalType;
import dev.econolyze.domain.enums.PaymentMethod;
import dev.econolyze.domain.enums.TransactionType;

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
}
