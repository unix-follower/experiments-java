package org.example.experiments.multithreading.ratelimiter;

public class ThrottlingException extends RateLimitException {
    private final String serviceName;
    private final String operationName;

    public ThrottlingException(String serviceName, String operationName, long retryAfterMillis) {
        super("Throttled", retryAfterMillis);
        this.serviceName = serviceName;
        this.operationName = operationName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getOperationName() {
        return operationName;
    }
}
