package org.example.experiments.multithreading.activeobject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class ActiveCreature {
    private final Logger logger = LoggerFactory.getLogger(ActiveCreature.class.getName());

    private final BlockingQueue<Runnable> requests = new LinkedBlockingQueue<>();
    private final String name;
    private final Thread thread;
    private final Object mutex;

    protected ActiveCreature(ActiveCreatureBuilder builder) {
        mutex = Objects.requireNonNullElse(builder.mutex, this);
        this.name = builder.name;
        final long millis = builder.reactionSpeed.toMillis();
        thread = new Thread(() -> {
            while (true) {
                try {
                    synchronized (mutex) {
                        logger.info("[mutex.wait()] {} starts reacting in {}ms", name, millis);
                        mutex.wait(millis);
                        logger.info("{} wake up", name);
                    }
                    requests.take().run();
                } catch (InterruptedException _) {
                    Thread.currentThread().interrupt();
                    logger.info("Received the interrupt signal. Perhaps, {} is dead", name);
                    break;
                }
            }
        }
        );
        thread.start();
    }

    public void eat() throws InterruptedException {
        requests.put((Runnable) () -> {
                    logger.info("{} is eating!", name());
                    logger.info("{} has finished eating!", name());
                }
        );
        synchronized (mutex) {
            mutex.notify();
            logger.info("Notify {} about the new eating task", name);
        }
    }

    public void roam() throws InterruptedException {
        requests.put((Runnable) () -> logger.info("{} has started to roam the wastelands.", name()));
        synchronized (mutex) {
            mutex.notify();
            logger.info("Notify {} about the new roaming task", name);
        }
    }

    public String name() {
        return this.name;
    }

    public void kill() {
        thread.interrupt();
        requests.clear();
    }

    public abstract static class ActiveCreatureBuilder {
        protected Object mutex;
        protected String name;
        protected Duration reactionSpeed;

        public ActiveCreatureBuilder mutex(Object mutex) {
            this.mutex = mutex;
            return this;
        }

        public ActiveCreatureBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ActiveCreatureBuilder reactionSpeed(Duration reactionSpeed) {
            this.reactionSpeed = reactionSpeed;
            return this;
        }

        public abstract ActiveCreature build();
    }
}
