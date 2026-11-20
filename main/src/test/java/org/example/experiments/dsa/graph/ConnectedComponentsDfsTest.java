package org.example.experiments.dsa.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ConnectedComponentsDfsTest {
    private static List<List<Integer>> createGraph(int nodes) {
        final List<List<Integer>> adjacencyListGraph = new ArrayList<>(nodes);
        for (int i = 0; i < nodes; i++) {
            adjacencyListGraph.add(new ArrayList<>());
        }
        return adjacencyListGraph;
    }

    private static void addUndirectedEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to);
        graph.get(to).add(from);
    }

    @Test
    void testConnectivity() {
        final int nodes = 11;
        final List<List<Integer>> graph = createGraph(nodes);

        // 0 --- 1       3 --- 6
        // |   / |       |     |
        // |  /  |       |     |
        // 7     2 - 5   4     9    10
        //
        //       8

        addUndirectedEdge(graph, 0, 1);
        addUndirectedEdge(graph, 1, 7);
        addUndirectedEdge(graph, 7, 0);
        addUndirectedEdge(graph, 1, 2);
        addUndirectedEdge(graph, 2, 5);
        addUndirectedEdge(graph, 3, 4);
        addUndirectedEdge(graph, 3, 6);
        addUndirectedEdge(graph, 6, 9);

        final var graphComponents = new ConnectedComponentsDfs(graph);

        Assertions.assertEquals(4, graphComponents.countComponents());
        for (int i = 0; i < nodes; i++) {
            System.out.printf("Node %d -> component %d%n", i, graphComponents.componentId(i));
        }
        // Components: {0,1,2,5,7} label 0, {3,4,6,9} label 1, {8} label 2, {10} label 3
        Assertions.assertArrayEquals(new int[]{0, 0, 0, 1, 1, 0, 1, 0, 2, 1, 3}, graphComponents.getComponentIds());
    }
}
