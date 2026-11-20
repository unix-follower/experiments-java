package org.example.experiments.streamapi;

import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class StreamSupportExamplesApp {
    private StreamSupportExamplesApp() {
    }

    static void main() {
        Spliterator.OfInt spliterator = IntStream.range(1, 10).spliterator();
        int characteristics = spliterator.characteristics();
        System.out.println("characteristics: " + characteristics);
        StreamSupport.intStream(spliterator, false).forEach(System.out::println);
    }
}
