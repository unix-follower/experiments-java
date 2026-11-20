package org.example.experiments.multithreading.ratelimiter;

public interface RateLimiter {
    void check(String serviceName, String operationName);
}
