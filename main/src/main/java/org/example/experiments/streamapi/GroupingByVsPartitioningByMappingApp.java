package org.example.experiments.streamapi;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GroupingByVsPartitioningByMappingApp {
    private GroupingByVsPartitioningByMappingApp() {
    }

    static void main() {
        final int[] ints = IntStream.range(1, 100).toArray();

        final Function<Integer, int[]> intArraySupplier = (number) -> new int[]{number % 2, number % 3, number % 4,
                number % 5, number % 6, number % 7, number % 8, number % 9, number % 10};
        final var map = Arrays.stream(ints)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), intArraySupplier));
        System.out.println(map);

        final Map<Integer, List<Integer>> grouppedMap = Arrays.stream(ints).boxed()
                .collect(Collectors.groupingBy(o -> (int) Math.ceil(Math.sqrt(o))));
        System.out.println(grouppedMap);
        final Map<Boolean, List<Integer>> partitionedMap = Arrays.stream(ints)
                .boxed()
                .collect(Collectors.partitioningBy(o -> (o % 2) == 0));
        System.out.println(partitionedMap);
    }
}
