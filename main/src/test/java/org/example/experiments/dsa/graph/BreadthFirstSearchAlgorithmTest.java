package org.example.experiments.dsa.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BreadthFirstSearchAlgorithmTest {
    @Test
    void testNullGraphInput() {
        assertThrows(
                NullPointerException.class, () -> new BreadthFirstSearchAlgorithm(null));
    }

    private static List<List<WeightedEdge>> createEmptyGraph(int nodes) {
        final List<List<WeightedEdge>> graph = new ArrayList<>(nodes);
        for (int i = 0; i < nodes; i++) {
            graph.add(new ArrayList<>());
        }
        return graph;
    }

    private static void addDirectedEdge(List<List<WeightedEdge>> graph, int from, int to) {
        graph.get(from).add(WeightedEdge.unweighted(from, to));
    }

    private static void addUndirectedEdge(List<List<WeightedEdge>> graph, int from, int to) {
        addDirectedEdge(graph, from, to);
        addDirectedEdge(graph, to, from);
    }

    @Test
    void testSingletonGraph() {
        // given
        final int nodes = 1;
        final List<List<WeightedEdge>> graph = createEmptyGraph(nodes);
        final var algorithm = new BreadthFirstSearchAlgorithm(graph);
        // when
        final List<Integer> path = algorithm.reconstructPath(0, 0);
        // then
        Assertions.assertEquals(List.of(0), path);
    }

    @Test
    void testTwoNodeGraph() {
        // given
        final int nodes = 2;
        final List<List<WeightedEdge>> graph = createEmptyGraph(nodes);
        addUndirectedEdge(graph, 0, 1);
        final var algorithm = new BreadthFirstSearchAlgorithm(graph);

        // when
        final List<Integer> path = algorithm.reconstructPath(0, 1);
        // then
        final var expected = List.of(0, 1);
        Assertions.assertEquals(expected, path);
    }

    @Test
    void testThreeNodeGraph() {
        // given
        final int nodes = 3;
        final List<List<WeightedEdge>> graph = createEmptyGraph(nodes);
        addUndirectedEdge(graph, 0, 1);
        addUndirectedEdge(graph, 2, 1);
        final var algorithm = new BreadthFirstSearchAlgorithm(graph);

        // when
        final List<Integer> path = algorithm.reconstructPath(0, 2);
        // then
        final List<Integer> expected = List.of(0, 1, 2);
        Assertions.assertEquals(expected, path);
    }

    @Test
    void testGraph() {
        // given
        final int nodes = 8;
        //   0 --- 1 --- 2
        //   |     |     |
        //   3     4     5
        //    \   / \   /
        //      6 --- 7
        final List<List<WeightedEdge>> graph = createEmptyGraph(nodes);

        addUndirectedEdge(graph, 0, 1);
        addUndirectedEdge(graph, 1, 2);
        addUndirectedEdge(graph, 0, 3);
        addUndirectedEdge(graph, 1, 4);
        addUndirectedEdge(graph, 2, 5);
        addUndirectedEdge(graph, 3, 6);
        addUndirectedEdge(graph, 4, 6);
        addUndirectedEdge(graph, 4, 7);
        addUndirectedEdge(graph, 5, 7);
        addUndirectedEdge(graph, 6, 7);

        // when
        final var algorithm = new BreadthFirstSearchAlgorithm(graph);
        // then
        Assertions.assertEquals(List.of(0, 1, 4, 7), algorithm.reconstructPath(0, 7));
        Assertions.assertEquals(List.of(3, 6, 7, 5), algorithm.reconstructPath(3, 5));
    }

    @Test
    void testShortestPathAgainstBellmanFord() {
        final int loops = 150;
        final var random = new Random();
        for (int i = 0, nodes = 1; i < loops; i++, nodes++) {
            // given
            final List<List<WeightedEdge>> adjListGraph = createEmptyGraph(nodes);

            final int startVertex = random.nextInt(1) * nodes;
            final int targetVertex = random.nextInt(1) * nodes;
            final var algorithm = new BreadthFirstSearchAlgorithm(adjListGraph); // NOPMD
            final double[][] adjMatrixGraph = generateRandomGraph(adjListGraph, nodes);

            // when
            final List<Integer> reconstructedPath = algorithm.reconstructPath(startVertex, targetVertex);
            // then
            final var edgeList = adjMatrixToEdgeList(adjMatrixGraph, nodes);
            final int expectedPathSize = calculatePathSize(edgeList, nodes, startVertex, targetVertex);
            Assertions.assertEquals(expectedPathSize, reconstructedPath.size());
        }
    }

    private List<WeightedEdge> adjMatrixToEdgeList(double[][] adjMatrixGraph, int nodes) {
        final List<WeightedEdge> edgeList = new ArrayList<>();
        for (int from = 0; from < nodes; from++) {
            for (int to = 0; to < nodes; to++) {
                if (from != to && adjMatrixGraph[from][to] != Double.POSITIVE_INFINITY) {
                    edgeList.add(new WeightedEdge(from, to, adjMatrixGraph[from][to]));
                }
            }
        }
        return edgeList;
    }

    private static int calculatePathSize(List<WeightedEdge> edgeList,
                                         int nodes, int startVertex, int targetVertex) {
        final WeightedEdge[] edges = edgeList.toArray(new WeightedEdge[0]);
        final double[] distances = BellmanFordEdgeList.bellmanFord(edges, nodes, startVertex);
        // All edges have weight 1, so BF distance equals number of edges = path size - 1
        return (distances[targetVertex] == Double.POSITIVE_INFINITY) ? 0 : (int) distances[targetVertex] + 1;
    }

    private static double[][] generateRandomGraph(List<List<WeightedEdge>> adjListGraph, int nodes) {
        final var random = new Random();

        final boolean[][] edgeMatrix = new boolean[nodes][nodes];
        final double[][] graph = new double[nodes][nodes];
        for (double[] row : graph) {
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        }

        final int numEdges = max(1, random.nextInt(1) * nodes * nodes);
        for (int i = 0; i < numEdges; i++) {
            final int from = random.nextInt(1) * nodes;
            final int to = random.nextInt(1) * nodes;
            if (!edgeMatrix[from][to]) {
                addUndirectedEdge(adjListGraph, from, to);
                graph[from][to] = graph[to][from] = 1;
                edgeMatrix[from][to] = edgeMatrix[to][from] = true;
            }
        }

        return graph;
    }
}
