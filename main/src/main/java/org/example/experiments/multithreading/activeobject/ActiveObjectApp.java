package org.example.experiments.multithreading.activeobject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;

@CommandLine.Command(mixinStandardHelpOptions = true, version = "1.0.0",
        description = "Execute the ActiveObject pattern scenarios.")
public final class ActiveObjectApp implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveObjectApp.class.getName());

    private static final int NUM_CREATURES = 3;

    @CommandLine.Option(names = {"-s", "--scenario"}, description = "The scenario name")
    private String scenario = "default";

    private ActiveObjectApp() {
    }

    static void main(String[] args) {
        new CommandLine(new ActiveObjectApp()).execute(args);

        LOGGER.info("Press ENTER to continue...");
        final var scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    @Override
    public void run() {
        LOGGER.info("scenario={}", scenario);

        final BiFunction<Integer, Duration, ActiveCreature> creatureCreator = (index, reactionSpeed) ->
                Orc.orc(Orc.class.getSimpleName() + index, reactionSpeed);
        switch (scenario.toLowerCase()) {
            case "sync-on-this" -> runScenario((index, reactionSpeed) ->
                    Orc.orcWithThisMutex(Orc.class.getSimpleName() + index, reactionSpeed));
            case "default" -> runScenario(creatureCreator);
            default -> {
                LOGGER.warn("Unknown scenario. Falling back to default.");
                runScenario(creatureCreator);
            }
        }
    }

    private void runScenario(BiFunction<Integer, Duration, ActiveCreature> activeCreatureCreator) {
        final List<ActiveCreature> creatures = new ArrayList<>();
        try {
            final var reactionSpeed = Duration.ofMillis(780);
            for (int i = 0; i < NUM_CREATURES; i++) {
                final ActiveCreature creature = activeCreatureCreator.apply(i, reactionSpeed);
                creatures.add(creature);
                creature.eat();
                creature.roam();
            }
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            for (var activeCreature : creatures) {
                activeCreature.kill();
            }
        }
    }
}
