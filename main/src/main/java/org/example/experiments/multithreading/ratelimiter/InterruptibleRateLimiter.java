package org.example.experiments.multithreading.ratelimiter;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class InterruptibleRateLimiter {
    private static final Logger LOGGER = Logger.getLogger(InterruptibleRateLimiter.class.getName());

    private final long maxTokens;
    private final long refillIntervalNanos;
    private long availableTokens;
    private long lastRefillTimeNanos;

    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition tokenAvailable = lock.newCondition();

    public InterruptibleRateLimiter(long permitsPerSecond) {
        if (permitsPerSecond <= 0) {
            throw new IllegalArgumentException("permitsPerSecond must be > 0.");
        }
        this.maxTokens = permitsPerSecond;
        this.availableTokens = permitsPerSecond;
        this.refillIntervalNanos = TimeUnit.SECONDS.toNanos(1) / permitsPerSecond;
        this.lastRefillTimeNanos = System.nanoTime();
    }

    /**
     * Acquires a token, blocking if necessary. Respects Thread.interrupt().
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public void acquire() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (true) {
                refill();

                if (availableTokens > 0) {
                    availableTokens--;
                    return;
                }

                // Calculate exact wait time for the next token
                long now = System.nanoTime();
                long nanosToWait = refillIntervalNanos - (now - lastRefillTimeNanos);

                if (nanosToWait <= 0) {
                    nanosToWait = refillIntervalNanos;
                }

                final long awaitNs = nanosToWait;
                LOGGER.info(() -> String.format(
                        "Await %d ms for tokens to be available%n", Duration.ofNanos(awaitNs).toMillis()));
                tokenAvailable.awaitNanos(nanosToWait);
            }
        } finally {
            lock.unlock();
        }
    }

    private void refill() {
        long now = System.nanoTime();
        long elapsed = now - lastRefillTimeNanos;

        if (elapsed > refillIntervalNanos) {
            long tokensToAdd = elapsed / refillIntervalNanos;
            if (tokensToAdd > 0) {
                availableTokens = Math.min(maxTokens, availableTokens + tokensToAdd);
                lastRefillTimeNanos += tokensToAdd * refillIntervalNanos;
                LOGGER.info("Notify waiting threads that tokens are available now, signalAll()");
                tokenAvailable.signalAll();
            }
        }
    }
}
