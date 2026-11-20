package org.example.experiments.multithreading.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowRateLimiter implements RateLimiter {
    private final int limit;
    private final long windowMillis;
    private final ConcurrentMap<String, WindowCounter> counters = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(int limit, long windowSeconds) {
        this.limit = limit;
        this.windowMillis = TimeUnit.SECONDS.toMillis(windowSeconds);
    }

    @Override
    public void check(String serviceName, String operationName) throws RateLimitException {
        synchronized (this) {
            final String key = serviceName + ":" + operationName;
            final WindowCounter counter = counters.computeIfAbsent(key, _ -> new WindowCounter());

            if (!counter.tryIncrement()) {
                throw new RateLimitException("Rate limit exceeded for " + key, windowMillis);
            }
        }
    }

    private class WindowCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private volatile long windowStart = System.currentTimeMillis();

        boolean tryIncrement() {
            synchronized (this) {
                final long now = System.currentTimeMillis();
                if (now - windowStart > windowMillis) {
                    count.set(0);
                    windowStart = now;
                }
                return count.incrementAndGet() <= limit;
            }
        }
    }
}
