package org.example.experiments.dsa.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class GraphUtils {
    private GraphUtils() {
    }

    public static <T> List<List<T>> createEmptyGraph(int numOfNodes) {
        if (numOfNodes < 0) {
            throw new IllegalArgumentException("numOfNodes cannot be negative; received: " + numOfNodes);
        }
        final List<List<T>> adjacencyListGraph = new ArrayList<>(numOfNodes);
        for (int i = 0; i < numOfNodes; i++) {
            adjacencyListGraph.add(new ArrayList<>());
        }
        return adjacencyListGraph;
    }

    public static void addDirectedWeightedEdge(List<List<WeightedEdge>> graph, int from, int to) {
        graph.get(from).add(WeightedEdge.unweighted(from, to));
    }

    public static void addUndirectedWeightedEdge(List<List<WeightedEdge>> graph, int from, int to) {
        addDirectedWeightedEdge(graph, from, to);
        addDirectedWeightedEdge(graph, to, from);
    }

    public static void addDirectedEdge(List<List<Integer>> graph, int from, int to) {
        Objects.requireNonNull(graph, "Graph cannot be null");
        int n = graph.size();
        if (from < 0 || from >= n) {
            throw new IllegalArgumentException("'from' node index out of bounds; received: " + from);
        }
        if (to < 0 || to >= n) {
            throw new IllegalArgumentException("'to' node index out of bounds; received: " + to);
        }
        graph.get(from).add(to);
    }

    /**
     * Adds an unweighted undirected edge from the node at index 'from' to the node at index 'to'.
     */
    public static void addUndirectedEdge(List<List<Integer>> graph, int from, int to) {
        addDirectedEdge(graph, from, to);
        addDirectedEdge(graph, to, from);
    }
}
