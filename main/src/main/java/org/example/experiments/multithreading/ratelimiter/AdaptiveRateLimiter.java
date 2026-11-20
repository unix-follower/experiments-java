package org.example.experiments.multithreading.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class AdaptiveRateLimiter implements AutoCloseable, RateLimiter {
    private static final Logger LOGGER = Logger.getLogger(AdaptiveRateLimiter.class.getName());

    private final int initialLimit;
    private final int maxLimit;
    private final AtomicInteger currentLimit;
    private final ConcurrentMap<String, RateLimiter> limiters = new ConcurrentHashMap<>();
    @SuppressWarnings("FieldCanBeLocal")
    private final ScheduledExecutorService healthChecker = Executors.newScheduledThreadPool(1);

    public AdaptiveRateLimiter(int initialLimit, int maxLimit) {
        this.initialLimit = initialLimit;
        this.maxLimit = maxLimit;
        this.currentLimit = new AtomicInteger(initialLimit);
        healthChecker.scheduleAtFixedRate(this::adjustLimits, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void close() {
        final var iterator = limiters.entrySet().iterator();

        while (iterator.hasNext()) {
            final var rateLimiter = iterator.next().getValue();
            if (rateLimiter instanceof AutoCloseable closeable) {
                try {
                    closeable.close();
                    // CHECKSTYLE:OFF
                } catch (Exception e) { // NOPMD
                    // CHECKSTYLE:ON
                    LOGGER.warning("Failed to close RateLimiter: " + e.getMessage());
                }
            }
            iterator.remove();
        }

        LOGGER.info("AdaptiveRateLimiter scheduler is shutting down");
        healthChecker.shutdown();
        boolean awaited = false;
        try {
            awaited = healthChecker.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
            LOGGER.warning("Interrupted while awaiting termination");
        }
        if (!awaited || !healthChecker.isShutdown() || !healthChecker.isTerminated()) {
            LOGGER.warning(() -> String.format("%s Force stopping scheduler", Thread.currentThread().getName()));
            healthChecker.shutdownNow();
        }
        healthChecker.close();
        LOGGER.info("AdaptiveRateLimiter scheduler shutdown");
    }

    @Override
    public void check(String serviceName, String operationName) throws RateLimitException {
        final String key = serviceName + ":" + operationName;
        final int current = currentLimit.get();
        final var limiter = limiters.computeIfAbsent(key, _ -> new TokenBucketRateLimiter(current, current));

        try {
            limiter.check(serviceName, operationName);
        } catch (RateLimitException e) {
            currentLimit.updateAndGet(curr -> Math.max(initialLimit, curr / 2));
            throw e;
        }
    }

    private void adjustLimits() {
        currentLimit.updateAndGet(curr -> Math.min(maxLimit, curr + (initialLimit / 2)));
    }
}
