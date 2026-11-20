package org.example.experiments.multithreading.ratelimiter;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * <a href="https://java-design-patterns.com/patterns/rate-limiting-pattern/">Inspired by</a>
 */
public final class RateLimiterApp {
    private static final Logger LOGGER = Logger.getLogger(RateLimiterApp.class.getName());

    private RateLimiterApp() {
    }

    static void main() throws InterruptedException {
        final var tokenBucketRateLimiter = new TokenBucketRateLimiter(2, 1);
        final var fixedWindowRateLimiter = new FixedWindowRateLimiter(3, 1);
        final var adaptiveRateLimiter = new AdaptiveRateLimiter(2, 6);

        final int numOfThreads = 3;
        final var latch = new CountDownLatch(numOfThreads);
        try (var executorSvc = Executors.newFixedThreadPool(numOfThreads);
             tokenBucketRateLimiter; adaptiveRateLimiter) {
            for (int i = 1; i <= 3; i++) {
                executorSvc.submit(createClientTask(i, i * 10, latch,
                        tokenBucketRateLimiter, fixedWindowRateLimiter, adaptiveRateLimiter));
            }

            LOGGER.info("The app thread pool is shutting down");
            executorSvc.shutdown();
            LOGGER.info("executorSvc.awaitTermination(10, TimeUnit.SECONDS)");
            final boolean awaited = executorSvc.awaitTermination(10, TimeUnit.SECONDS);
            if (!awaited || !executorSvc.isShutdown() || !executorSvc.isTerminated()) {
                LOGGER.warning("Await timeout has expired! Killing the unfinished tasks");
                executorSvc.shutdownNow();
            }
        }
        LOGGER.info("Outside of the try block");

        LOGGER.info("Press ENTER to continue...");
        final var scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    private static Runnable createClientTask(
            int clientId,
            int minIterations,
            CountDownLatch latch,
            RateLimiter tokenBucketRateLimiter,
            RateLimiter fixedWindowRateLimiter,
            RateLimiter adaptiveRateLimiter) {
        final var random = new Random();

        return () -> {
            final String[] services = {"s3", "dynamodb", "lambda"};
            final String[] operations = {"GetObject", "PutObject", "Query", "Scan",
                    "PutItem", "Invoke", "ListFunctions"};

            long iterations = 0;
            while (true) {
                final String service = services[random.nextInt(services.length)];
                final String operation = operations[random.nextInt(operations.length)];
                try {
                    switch (service) {
                        case "s3" -> tokenBucketRateLimiter.check(service, operation);
                        case "dynamodb" -> fixedWindowRateLimiter.check(service, operation);
                        case "lambda" -> adaptiveRateLimiter.check(service, operation);
                        default -> throw new IllegalStateException("Unexpected value: " + service);
                    }
                    LOGGER.info(String.format("Client %d: %s.%s - ALLOWED%n", clientId, service, operation));
                } catch (RateLimitException _) {
                    LOGGER.info(String.format("Client %d: %s.%s - THROTTLED%n", clientId, service, operation));
                }

                try {
                    Thread.sleep(30L + random.nextInt(50));
                } catch (InterruptedException _) {
                    Thread.currentThread().interrupt();
                }

                if (iterations >= minIterations && latch.getCount() == 0) {
                    break;
                }
                LOGGER.info(String.format(
                        "%s, latch.getCount()=%d", Thread.currentThread().getName(), latch.getCount()));
                iterations++;
                latch.countDown();
            }

            LOGGER.info(String.format("%s has finished. iterations=%d", Thread.currentThread().getName(), iterations));
        };
    }
}
