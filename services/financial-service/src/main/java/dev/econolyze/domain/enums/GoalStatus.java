package dev.econolyze.domain.enums;

import lombok.Getter;

@Getter
public enum GoalStatus {
    ACTIVE(1),
    COMPLETED(2),
    CANCELLED(3),
    PAUSED(4),
    OVERDUE(5),
    DRAFT(6);

    private final int code;

    GoalStatus(int code){
        this.code = code;
    }
    public static GoalStatus fromCode(int code){
        for(GoalStatus s : GoalStatus.values()){
            if(s.code == code)
                return s;
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
