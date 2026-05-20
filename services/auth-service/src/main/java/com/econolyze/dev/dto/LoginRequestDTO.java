package com.econolyze.dev.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;

@RegisterForReflection
public class LoginRequestDTO {
    @NotBlank
    public String username;
    @NotBlank
    public String password;
}
