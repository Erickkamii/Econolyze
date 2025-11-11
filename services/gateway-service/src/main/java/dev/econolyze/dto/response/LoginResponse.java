package dev.econolyze.dto.response;

public record LoginResponse(
        String username,
        String authToken,
        String refreshToken
) {
}
