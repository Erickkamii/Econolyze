package dev.econolyze.domain.enums;

import lombok.Getter;

@Getter
public enum RecurrenceFrequency {
    NONE,
    MONTHLY,
    YEARLY,
    WEEKLY,
    DAILY,
    BIWEEKLY,
    QUARTERLY,
    SEMIANNUALLY,
    OTHER;
}
