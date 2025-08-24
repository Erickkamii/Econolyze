package dev.econolyze.enums;


import lombok.Getter;

@Getter
public enum Category {
    FOOD(1),
    HOUSEHOLD(2),
    TRANSPORT(3),
    HEALTH(4),
    INSURANCE(5),
    UTILITIES(6),
    OTHER(7);

    private final int code;

    Category(int code) {
        this.code = code;
    }

    public static Category fromCode(int code) {
        for (Category c : Category.values()) {
            if (c.getCode() == code) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
