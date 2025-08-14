package com.econolyze.dev.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private Long userId;
    private String username;
    private String authToken;
    private String refreshToken;
}
