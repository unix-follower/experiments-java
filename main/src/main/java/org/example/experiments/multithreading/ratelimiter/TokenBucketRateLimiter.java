package org.example.experiments.multithreading.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class TokenBucketRateLimiter implements AutoCloseable, RateLimiter {
    private static final Logger LOGGER = Logger.getLogger(TokenBucketRateLimiter.class.getName());

    private final int capacity;
    private final int refillRate;
    private final ConcurrentMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    @SuppressWarnings("FieldCanBeLocal")
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public TokenBucketRateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        scheduler.scheduleAtFixedRate(this::refillBuckets, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void close() {
        LOGGER.info("TokenBucketRateLimiter scheduler is shutting down");
        scheduler.shutdown();
        boolean awaited = false;
        try {
            awaited = scheduler.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
            LOGGER.warning("Interrupted while awaiting termination");
        }
        buckets.clear();
        if (!awaited || !scheduler.isShutdown() || !scheduler.isTerminated()) {
            LOGGER.warning(() -> String.format("%s Force stopping scheduler", Thread.currentThread().getName()));
            scheduler.shutdownNow();
        }
        scheduler.close();
        LOGGER.info("TokenBucketRateLimiter scheduler shutdown");
    }

    @Override
    public void check(String serviceName, String operationName) throws RateLimitException {
        final String key = serviceName + ":" + operationName;
        final TokenBucket bucket = buckets.computeIfAbsent(key, k -> new TokenBucket(capacity));
        if (!bucket.tryConsume()) {
            throw new ThrottlingException(serviceName, operationName, 1000);
        }
    }

    private void refillBuckets() {
        buckets.forEach((_, bucket) -> bucket.refill(refillRate));
    }

    private static class TokenBucket {
        private final int capacity;
        private final AtomicInteger tokens;

        TokenBucket(int capacity) {
            this.capacity = capacity;
            this.tokens = new AtomicInteger(capacity);
        }

        boolean tryConsume() {
            while (true) {
                int current = tokens.get();
                if (current <= 0) {
                    return false;
                }
                if (tokens.compareAndSet(current, current - 1)) {
                    return true;
                }
            }
        }

        void refill(int amount) {
            tokens.getAndUpdate(current -> Math.min(current + amount, capacity));
        }
    }
}
