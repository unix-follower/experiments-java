package org.example.experiments.multithreading.ratelimiter;

import java.io.IOException;
import java.util.logging.Logger;

public class RateLimiterWithThreadInterruptApp {
    private static final Logger LOGGER = Logger.getLogger(RateLimiterWithThreadInterruptApp.class.getName());

    private RateLimiterWithThreadInterruptApp() {
    }

    static void main() throws InterruptedException, IOException {
        // Allow 2 permits per second
        InterruptibleRateLimiter limiter = new InterruptibleRateLimiter(2);

        Thread worker = new Thread(() -> {
            LOGGER.info("Worker started, attempting rate-limited tasks...");
            try {
                for (int i = 1; i <= 10; i++) {
                    limiter.acquire(); // Respects interruption here
                    final int idx = i;
                    LOGGER.info(() -> "Executed task " + idx + " at " + System.currentTimeMillis());
                }
            } catch (InterruptedException _) {
                LOGGER.warning("Worker was interrupted while waiting for rate limiter token!");
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        });

        worker.start();

        Thread.sleep(3000);
        LOGGER.info("Interrupting worker thread...");
        worker.interrupt();

        worker.join();
        LOGGER.info("Worker has stopped cleanly.");

        LOGGER.info("Press ENTER to continue...");
        System.in.read();
    }
}
