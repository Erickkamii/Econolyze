package dev.econolyze.exception;
/**
 * Exception thrown when a required service is unavailable.
 */
public class ServiceUnavailableException extends RuntimeException {
    private final String serviceName;

    public ServiceUnavailableException(String serviceName) {
        super(String.format("Service %s is unavailable", serviceName));
        this.serviceName = serviceName;
    }

    public ServiceUnavailableException(String serviceName, Throwable cause) {
        super(String.format("Service %s is unavailable", serviceName), cause);
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
