package org.example.experiments.streamapi;

import java.util.Random;
import java.util.stream.Stream;

public class IterateVsGenerateApp {
    private IterateVsGenerateApp() {
    }

    static void main() {
        Stream.iterate(0, i -> i + 1).limit(10).forEach(System.out::println);

        final var random = new Random(42);
        Stream.generate(() -> random.nextGaussian(30, 5)).limit(10).forEach(System.out::println);
    }
}
