package org.example.experiments.streamapi;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.stream.IntStream;

@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 2, time = 1)
@Threads(2)
@Fork(1)
public class CollectionForEachVsStreamForEachBenchmark {
    @State(Scope.Benchmark)
    public static class NumbersState {
        @Param({"10", "1000", "100000"})
        int size;

        List<Integer> numbers;

        @Setup
        public void setUp() {
            numbers = IntStream.range(0, size).boxed().toList();
        }
    }

    @Benchmark
    public void collectionForEach(Blackhole blackhole, NumbersState state) {
        state.numbers.forEach(blackhole::consume);
    }

    @Benchmark
    public void streamForEach(Blackhole blackhole, NumbersState state) {
        state.numbers.stream().forEach(blackhole::consume);
    }
}
