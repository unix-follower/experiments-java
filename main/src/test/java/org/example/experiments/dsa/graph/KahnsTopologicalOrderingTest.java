package org.example.experiments.dsa.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class KahnsTopologicalOrderingTest {
    @Test
    void testCycleInGraph() {
        // given
        final List<List<Integer>> graph = GraphUtils.createEmptyGraph(4);
        GraphUtils.addDirectedEdge(graph, 0, 1);
        GraphUtils.addDirectedEdge(graph, 1, 2);
        GraphUtils.addDirectedEdge(graph, 2, 3);
        GraphUtils.addDirectedEdge(graph, 3, 0);
        // when / then
        Assertions.assertThrows(IllegalArgumentException.class, () -> KahnsTopologicalOrdering.topologicalOrder(graph));
    }

    @Test
    void testRandomDAG() {
        // given
        final var dagGen = new DagGenerator(10, 10, 5, 5, 0.86);
        final List<List<Integer>> graph = dagGen.createDag();
        // when
        final int[] order = KahnsTopologicalOrdering.topologicalOrder(graph);
        // then
        Assertions.assertTrue(TopologicalSort.isTopsortOrdering(graph, order));
    }

    @Test
    void testRandomDAGs() {
        for (double probability = 0.7; probability <= 1.0; probability += 0.02) {
            // given
            final var dagGen = new DagGenerator(2, 20, 4, 15, probability); // NOPMD
            final List<List<Integer>> graph = dagGen.createDag();
            // when
            final int[] order = KahnsTopologicalOrdering.topologicalOrder(graph);
            // then
            Assertions.assertTrue(TopologicalSort.isTopsortOrdering(graph, order));
        }
    }

    @Test
    void test14VertexDAG() {
        // given
        final List<List<Integer>> graph = GraphUtils.createEmptyGraph(14);
        GraphUtils.addDirectedEdge(graph, 0, 2);
        GraphUtils.addDirectedEdge(graph, 0, 3);
        GraphUtils.addDirectedEdge(graph, 0, 6);
        GraphUtils.addDirectedEdge(graph, 1, 4);
        GraphUtils.addDirectedEdge(graph, 2, 6);
        GraphUtils.addDirectedEdge(graph, 3, 1);
        GraphUtils.addDirectedEdge(graph, 3, 4);
        GraphUtils.addDirectedEdge(graph, 4, 5);
        GraphUtils.addDirectedEdge(graph, 4, 8);
        GraphUtils.addDirectedEdge(graph, 6, 7);
        GraphUtils.addDirectedEdge(graph, 6, 11);
        GraphUtils.addDirectedEdge(graph, 7, 4);
        GraphUtils.addDirectedEdge(graph, 7, 12);
        GraphUtils.addDirectedEdge(graph, 9, 2);
        GraphUtils.addDirectedEdge(graph, 9, 10);
        GraphUtils.addDirectedEdge(graph, 10, 6);
        GraphUtils.addDirectedEdge(graph, 11, 12);
        GraphUtils.addDirectedEdge(graph, 12, 8);
        // when
        final int[] ordering = KahnsTopologicalOrdering.topologicalOrder(graph);
        // then
        Assertions.assertArrayEquals(new int[]{0, 9, 13, 3, 2, 10, 1, 6, 7, 11, 4, 12, 5, 8}, ordering);
    }
}
