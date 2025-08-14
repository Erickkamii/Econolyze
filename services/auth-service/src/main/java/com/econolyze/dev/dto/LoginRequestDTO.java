package com.econolyze.dev.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {
    @NotBlank
    public String username;
    @NotBlank
    public String password;
}
