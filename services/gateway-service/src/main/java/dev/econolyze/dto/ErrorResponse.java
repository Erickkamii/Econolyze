package dev.econolyze.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        List<ValidationError> errors,
        Map<String, Object> details
) {
    // Construtor para erro simples
    public ErrorResponse(int status, String error, String message, String path) {
        this(status, error, message, path, LocalDateTime.now(), null, null);
    }

    // Construtor para validação
    public ErrorResponse(int status, String error, String message, String path, List<ValidationError> errors) {
        this(status, error, message, path, LocalDateTime.now(), errors, null);
    }

    // Construtor com detalhes
    public ErrorResponse(int status, String error, String message, String path, Map<String, Object> details) {
        this(status, error, message, path, LocalDateTime.now(), null, details);
    }

    public record ValidationError(
            String field,
            String message,
            Object rejectedValue
    ) {}
}