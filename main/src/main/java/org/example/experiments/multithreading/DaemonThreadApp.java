package org.example.experiments.multithreading;

import java.util.logging.Logger;

public class DaemonThreadApp {
    private static final Logger LOGGER = Logger.getLogger(DaemonThreadApp.class.getName());

    private DaemonThreadApp() {
    }

    static void main() {
        Thread.ofPlatform().name("worker").start(() -> {
            try {
                Thread.sleep(2000);
                LOGGER.info(Thread.currentThread().getName() + " woke up");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });

        Thread.ofPlatform().name("daemon").daemon(true).start(() -> {
            try {
                // Main and worker threads should end first and JVM will exit before daemon gets completed
                Thread.sleep(5000);
                LOGGER.info(Thread.currentThread().getName() + " woke up");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });

        LOGGER.info("Main is exiting...");
    }
}
