package dev.econolyze.dto;

public record LoginResponse(
        Long userId,
        String username,
        String authToken,
        String refreshToken
) {
}
