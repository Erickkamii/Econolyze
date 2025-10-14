package dev.econolyze.exception;

import dev.econolyze.dto.ErrorResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionHandler {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

    @Context
    UriInfo uriInfo;

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> mapException(Throwable exception) {
        LOG.errorf(exception, "Exception caught: %s", exception.getMessage());

        int status;
        String error;
        String message;
        Map<String, Object> details = new HashMap<>();

        status = RestResponse.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        error = "Internal Server Error";
        message = "An unexpected error occurred";

        if (isDevMode()) {
            details.put("exceptionType", exception.getClass().getName());
            details.put("exceptionMessage", exception.getMessage());
        }
        String path = uriInfo != null ? uriInfo.getPath() : "unknown";

        ErrorResponse errorResponse = new ErrorResponse(
                status,
                error,
                message,
                path,
                details.isEmpty() ? null : details);
        return RestResponse.status(RestResponse.Status.fromStatusCode(status), errorResponse);
    }

    private boolean isDevMode() {
        return "dev".equals(System.getProperty("quarkus.profile"));
    }
}
