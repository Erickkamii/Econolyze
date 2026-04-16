package dev.econolyze.domain.enums;

import lombok.Getter;

@Getter
public enum GoalStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED,
    PAUSED,
    OVERDUE,
    DRAFT
}
