package com.econolyze.dev.dto;

import com.econolyze.dev.util.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO (
    @NotBlank(message = "Informe o nome de usuário")
    String username,
    @Email
    @NotBlank(message="Informe o email")
    String email,
    @StrongPassword
    String password,
    String roles
){}
