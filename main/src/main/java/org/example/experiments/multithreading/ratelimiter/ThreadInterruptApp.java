package org.example.experiments.multithreading.ratelimiter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadInterruptApp {
    private static final Logger LOGGER = Logger.getLogger(ThreadInterruptApp.class.getName());

    private ThreadInterruptApp() {
    }

    static void main() throws IOException {
        final Thread t1 = Thread.ofPlatform().name("t1").start(() -> {
            try {
                LOGGER.info("t1 is going to sleep for 5 seconds");
                Thread.sleep(5000);
                LOGGER.info("t1 woke up");
            } catch (InterruptedException e) {
                LOGGER.warning(e.getMessage());
                Thread.currentThread().interrupt();
            }
        });

        try {
            LOGGER.info("main is going to sleep for 1 second");
            Thread.sleep(1000);
            LOGGER.info("main woke up");
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Interrupted", e);
            Thread.currentThread().interrupt();
        }
        LOGGER.info("main is going to interrupt t1");
        t1.interrupt();

        LOGGER.info("Press ENTER to continue...");
        System.in.read();
    }
}
