package dev.econolyze.dto.response;

public record GoalProgressResponse(
    Long id,
    String name,
    Double progress
) {}
