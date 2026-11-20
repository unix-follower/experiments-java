package org.example.experiments.multithreading.activeobject;

import java.time.Duration;

public class Orc extends ActiveCreature {
    private Orc(OrcBuilder builder) {
        super(builder);
    }

    private static class OrcBuilder extends ActiveCreatureBuilder {
        @Override
        public ActiveCreature build() {
            return new Orc(this);
        }
    }

    public static ActiveCreature orc(String name, Duration reactionSpeed) {
        return new OrcBuilder()
                .name(name)
                .reactionSpeed(reactionSpeed)
                .mutex(new Object())
                .build();
    }

    public static ActiveCreature orcWithThisMutex(String name, Duration reactionSpeed) {
        return new OrcBuilder()
                .name(name)
                .reactionSpeed(reactionSpeed)
                .build();
    }
}
