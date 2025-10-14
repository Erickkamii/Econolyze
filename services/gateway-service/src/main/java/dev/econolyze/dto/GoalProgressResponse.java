package dev.econolyze.dto;

public record GoalProgressResponse(
    Long id,
    String name,
    Double progress
) {}
