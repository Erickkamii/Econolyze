package com.econolyze.dev.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private Long userId;
    private String username;
    private String authToken;
    private String refreshToken;
}
