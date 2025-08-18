package com.econolyze.dev.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;

import java.util.Set;
import java.util.stream.Collectors;

@Provider
public class AuthExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception){
        Set<ConstraintViolation<?>> violation = exception.getConstraintViolations();
        String message = violation.iterator().next().getMessage();
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(message))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    public static class ErrorResponse {
        public String message;
        public int status = 400;
        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
