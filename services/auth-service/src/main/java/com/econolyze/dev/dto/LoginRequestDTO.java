package com.econolyze.dev.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
