package com.econolyze.dev.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.Set;

public class AuthExceptionHandler {

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        String message = violations.iterator().next().getMessage();

        return RestResponse.status(Response.Status.BAD_REQUEST, new ErrorResponse(message, 400));
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleWrongCredentials(WrongCredentialsException exception) {
        return RestResponse.status(Response.Status.UNAUTHORIZED, new ErrorResponse(exception.getMessage(), 401));
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleInvalidToken(InvalidTokenException exception) {
        return RestResponse.status(Response.Status.UNAUTHORIZED, new ErrorResponse(exception.getMessage(), 401));
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleGenericException(Exception exception) {
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR,
                new ErrorResponse("Erro interno: " + exception.getMessage(), 500));
    }
}
