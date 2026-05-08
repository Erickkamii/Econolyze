package com.econolyze.dev.interfaces.rest.dto;

import java.math.BigDecimal;

public record GoalDTO(
        Long id,
        Long userId,
        String name,
        BigDecimal amount,
        String description,
        String type,
        String status
) {
}
