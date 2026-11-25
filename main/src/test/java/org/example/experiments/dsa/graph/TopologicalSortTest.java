package org.example.experiments.dsa.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TopologicalSortTest {
    private static Map<Integer, List<WeightedEdge>> createGraph() {
        final int nodes = 7;
        final var graph = new HashMap<Integer, List<WeightedEdge>>();
        for (int i = 0; i < nodes; i++) {
            graph.put(i, new ArrayList<>()); // NOPMD
        }

        //  0 --(3)---- 1
        // /  \       /  \
        // (3) (2)  (6)  (1)
        // |   /   /      |
        // |  |  /        |
        // |  2  --(1)-- 3
        // |  |         /
        // |  (10)     (5)
        // |  |        /
        // |  4 ----- /
        // |  |
        // |  (7)
        // \  |
        //   5
        graph.get(0).add(new WeightedEdge(0, 1, 3));
        graph.get(0).add(new WeightedEdge(0, 2, 2));
        graph.get(0).add(new WeightedEdge(0, 5, 3));
        graph.get(1).add(new WeightedEdge(1, 3, 1));
        graph.get(1).add(new WeightedEdge(1, 2, 6));
        graph.get(2).add(new WeightedEdge(2, 3, 1));
        graph.get(2).add(new WeightedEdge(2, 4, 10));
        graph.get(3).add(new WeightedEdge(3, 4, 5));
        graph.get(5).add(new WeightedEdge(5, 4, 7));
        return graph;
    }

    @Test
    void testTopologicalSort() {
        // given
        final var graph = createGraph();
        // when
        final int[] ordering = TopologicalSort.topologicalSort(graph, graph.size());
        // then
        Assertions.assertArrayEquals(new int[]{6, 0, 5, 1, 2, 3, 4}, ordering);
    }

    @Test
    void testDagShortestPath() {
        // given
        final var graph = createGraph();
        // when
        final Double[] distances = TopologicalSort.dagShortestPath(graph, 0, graph.size());
        // then
        final var expectedDistances = new Double[]{
                0., 3., 2.,
                3., // [0] -> [2] -> [3]; 2+1=3
                // Alternative: [0] -> [1] -> [3]; 3+1=4
                8., // [0] -> [2] -> [3] -> [4]; 2+1+5=8
                // Alternative: [0] -> [1] -> [3] -> [4]; 3+1+5=9
                3.,
                null // Unreachable
        };
        Assertions.assertArrayEquals(expectedDistances, distances);
    }

    @Test
    void testVerifyIsTopsortOrdering() {
        // given
        final List<List<Integer>> graph = GraphUtils.createEmptyGraph(3);
        GraphUtils.addDirectedEdge(graph, 0, 1);
        GraphUtils.addDirectedEdge(graph, 1, 2);
        final int[] order = {2, 1, 0};
        // when
        final boolean ordering = TopologicalSort.isTopsortOrdering(graph, order);
        // then
        Assertions.assertFalse(ordering);
    }
}
