package org.example.experiments.streamapi;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class CollectionForEachVsStreamForEachApp {
    private CollectionForEachVsStreamForEachApp() {
    }

    static void printNumbers(int[] numbers) {
        final IntStream intStream = Arrays.stream(numbers);
        intStream.forEach(System.out::println);
    }

    static void printNumbers(Collection<Integer> numbers) {
        numbers.forEach(System.out::println);
    }

    static void main() {
        final int[] ints = IntStream.range(1, 10).toArray();

        printNumbers(ints);

        final List<Integer> numbers = StreamSupport.stream(Arrays.spliterator(ints), false).toList();
        printNumbers(numbers);
    }
}
