package org.example.experiments.streamapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FailFastVsFailSafeApp {
    private FailFastVsFailSafeApp() {
    }

    static void main() {
        final List<Integer> numbers = Stream.iterate(0, i -> i + 1).limit(10).toList();

        failFast(new ArrayList<>(numbers));
        failFast(new LinkedList<>(numbers));
        failFast(new HashSet<>(numbers));
        failFast(new LinkedHashSet<>(numbers));
        failFast(new TreeSet<>(numbers));

        final var symbolicNumbers = new LinkedHashSet<String>();
        symbolicNumbers.add("zero");
        symbolicNumbers.add("one");
        symbolicNumbers.add("two");
        symbolicNumbers.add("three");
        symbolicNumbers.add("four");
        symbolicNumbers.add("five");
        symbolicNumbers.add("six");
        symbolicNumbers.add("seven");
        symbolicNumbers.add("eight");
        symbolicNumbers.add("nine");
        final var symbolicList = new ArrayList<>(symbolicNumbers);
        final Map<String, Integer> numberMap = IntStream.range(0, Math.min(numbers.size(), symbolicList.size()))
                .mapToObj(i -> Map.entry(symbolicList.get(i), numbers.get(i)))
                .peek(System.out::println)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (a, _) -> a, LinkedHashMap::new));
        failFast(new HashMap<>(numberMap));
        failFast(new PriorityQueue<>(numbers), PriorityQueue::new);

        failSafe(new CopyOnWriteArrayList<>(numbers));
        failSafe(new CopyOnWriteArraySet<>(numbers));
        failSafe(new LinkedBlockingQueue<>(numbers), LinkedBlockingQueue::new);
        failSafe(new LinkedBlockingDeque<>(numbers), LinkedBlockingDeque::new);
        failSafe(new ConcurrentSkipListSet<>(numbers));
        failSafe(new ConcurrentHashMap<>(numberMap));

        fail(new ArrayBlockingQueue<>(numbers.size(), true, numbers), () -> new ArrayBlockingQueue<>(numbers.size()));

//        java.lang.OutOfMemoryError: Java heap space
//        ConcurrentLinkedQueue
//        ConcurrentLinkedDeque
//        ConcurrentSkipListMap
    }

    private static void failFast(List<Integer> numbers) {
        try {
            numbers.stream()
                    .map(number -> {
                        numbers.add(number * 2);
                        return number;
                    })
                    .toList();
        } catch (ConcurrentModificationException e) {
            System.err.println(e);
        }
    }

    private static void failFast(Set<Integer> numbers) {
        try {
            numbers.stream()
                    .map(number -> {
                        numbers.add(number * 2);
                        return number;
                    })
                    .collect(Collectors.toSet());
        } catch (ConcurrentModificationException e) {
            System.err.println(e);
        }
    }

    private static void failFast(Queue<Integer> numbers, Supplier<? extends Queue<Integer>> queueSupplier) {
        try {
            numbers.stream()
                    .map(number -> {
                        numbers.add(number * 2);
                        return number;
                    })
                    .collect(Collectors.toCollection(queueSupplier));
        } catch (ConcurrentModificationException e) {
            logFailFast(numbers.getClass());
        }
    }

    private static void logFailFast(Class<?> collectionClass) {
        final String className = collectionClass.getName();
        System.err.println(className);
    }

    private static void failFast(Map<String, Integer> numberMap) {
        try {
            numberMap.entrySet().stream()
                    .map(entry -> {
                        numberMap.put(entry.getKey() + entry.getValue(), entry.getValue() * 2);
                        return entry;
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (ConcurrentModificationException e) {
            logFailFast(numberMap.getClass());
        }
    }

    private static void logFailSafe(Collection<Integer> numbers, Collection<Integer> results) {
        final String className = numbers.getClass().getName();
        System.out.printf("%s: numbers.size(): %d, results.size(): %d%n", className, numbers.size(), results.size());
    }

    private static void failSafe(List<Integer> numbers) {
        final List<Integer> results = numbers.stream()
                .map(number -> {
                    numbers.add(number * 2);
                    return number;
                })
                .toList();
        logFailSafe(numbers, results);
    }

    private static void fail(Queue<Integer> numbers, Supplier<? extends Queue<Integer>> queueSupplier) {
        try {
            numbers.stream()
                    .map(number -> {
                        numbers.add(number * 2);
                        return number;
                    })
                    .collect(Collectors.toCollection(queueSupplier));
        } catch (IllegalStateException e) {
            System.err.println(e);
        }
    }

    private static void failSafe(Queue<Integer> numbers, Supplier<? extends Queue<Integer>> queueSupplier) {
        final Queue<Integer> results = numbers.stream()
                .map(number -> {
                    numbers.add(number * 2);
                    return number;
                })
                .collect(Collectors.toCollection(queueSupplier));
        logFailSafe(numbers, results);
    }

    private static void failSafe(Deque<Integer> numbers, Supplier<? extends Deque<Integer>> queueSupplier) {
        final Queue<Integer> results = numbers.stream()
                .map(number -> {
                    numbers.add(number * 2);
                    return number;
                })
                .collect(Collectors.toCollection(queueSupplier));
        logFailSafe(numbers, results);
    }

    private static void failSafe(Set<Integer> numbers) {
        final Set<Integer> results = numbers.stream()
                .map(number -> {
                    numbers.add(number * 2);
                    return number;
                })
                .collect(Collectors.toSet());
        logFailSafe(numbers, results);
    }

    private static void failSafe(Map<String, Integer> numberMap) {
        final Map<String, Integer> results = numberMap.entrySet().stream()
                .map(entry -> {
                    numberMap.put(entry.getKey() + entry.getValue(), entry.getValue() * 2);
                    return entry;
                })
                .collect(Collectors.toMap(
                        entry -> entry.getKey() + entry.getValue(), Map.Entry::getValue));
        final String className = numberMap.getClass().getName();
        System.out.printf("%s: numberMap.size(): %d, results.size(): %d%n",
                className, numberMap.size(), results.size());
    }
}
