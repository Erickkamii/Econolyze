package dev.econolyze.mapper;

import dev.econolyze.dto.response.ErrorResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.Map;

public class JwtExceptionMapper {

    private static final Logger LOG = Logger.getLogger(JwtExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> mapUnauthorizedException(Exception exception) {
        LOG.warnf("Unauthorized access: %s", exception.getMessage());

        String path = uriInfo != null ? uriInfo.getPath() : "unknown";

        ErrorResponse errorResponse = new ErrorResponse(
                RestResponse.Status.UNAUTHORIZED.getStatusCode(),
                "Unauthorized",
                "You are not authorized to access this resource.",
                path,
                Map.of(
                        "hint", "Please login to get a new token",
                        "loginEndpoint", "/auth/login"
                )
        );

        return RestResponse.status(RestResponse.Status.UNAUTHORIZED, errorResponse);
    }
}
