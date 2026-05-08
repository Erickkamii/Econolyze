package com.econolyze.dev.interfaces.rest.dto;


import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank String message
        ) {
}
