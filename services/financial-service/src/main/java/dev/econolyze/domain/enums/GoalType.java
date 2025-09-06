package dev.econolyze.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum GoalType {
    VACATION(1),
    TRAVEL(2),
    INVESTMENT(3),
    SAVING(4),
    OTHER(5);

    private final int code;

    GoalType(int code){
        this.code = code;
    }

    public static GoalType fromCode(int code){
        for(GoalType type : GoalType.values()){
            if(type.code == code)
                return type;
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    @JsonCreator
    public static GoalType fromString(String value) {
        return GoalType.valueOf(value.toUpperCase());
    }
    @JsonValue
    public String toJson() {
        return this.name();
    }
}
