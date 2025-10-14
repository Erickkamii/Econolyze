package dev.econolyze.mapper;

import dev.econolyze.dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationExceptionMapper {

    private static final Logger LOG = Logger.getLogger(ValidationExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> mapConstraintViolation(ConstraintViolationException exception) {
        LOG.warnf("Validation failed: %s", exception.getMessage());

        List<ErrorResponse.ValidationError> errors = exception.getConstraintViolations()
                .stream()
                .map(this::toValidationError)
                .collect(Collectors.toList());

        String path = uriInfo != null ? uriInfo.getPath() : "unknown";

        ErrorResponse errorResponse = new ErrorResponse(
                RestResponse.Status.BAD_REQUEST.getStatusCode(),
                "Validation Failed",
                "Request validation failed. Please check the errors.",
                path,
                errors
        );

        return RestResponse.status(RestResponse.Status.BAD_REQUEST, errorResponse);
    }

    private ErrorResponse.ValidationError toValidationError(ConstraintViolation<?> violation) {
        String field = violation.getPropertyPath().toString();
        String message = violation.getMessage();
        Object rejectedValue = violation.getInvalidValue();

        return new ErrorResponse.ValidationError(field, message, rejectedValue);
    }
}