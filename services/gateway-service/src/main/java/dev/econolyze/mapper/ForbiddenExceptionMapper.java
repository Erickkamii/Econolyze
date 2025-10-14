package dev.econolyze.mapper;

import dev.econolyze.dto.ErrorResponse;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class ForbiddenExceptionMapper {

    private static final Logger LOG = Logger.getLogger(ForbiddenExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> mapForbiddenException(ForbiddenException exception) {
        LOG.warnf("Forbidden access: %s", exception.getMessage());

        String path = uriInfo != null ? uriInfo.getPath() : "unknown";
        ErrorResponse errorResponse = new ErrorResponse(
                RestResponse.Status.FORBIDDEN.getStatusCode(),
                "Forbidden",
                "You are not authorized to access this resource.",
                path
        );
        return RestResponse.status(RestResponse.Status.FORBIDDEN, errorResponse);
    }
}
