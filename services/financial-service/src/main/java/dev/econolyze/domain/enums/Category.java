package dev.econolyze.domain.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Category {
    FOOD(1),
    HOUSEHOLD(2),
    TRANSPORT(3),
    HEALTH(4),
    INSURANCE(5),
    INVESTMENT(6),
    UTILITIES(7),
    OTHER(8);

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


    @JsonCreator
    public static Category fromString(String value) {
        return Category.valueOf(value.toUpperCase());
    }
    @JsonValue
    public String toJson() {
        return this.name();
    }
}
