package org.example.experiments.dsa.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ConnectedComponentsDfsTest {
    @Test
    void testConnectivity() {
        final int nodes = 11;
        final List<List<Integer>> graph = GraphUtils.createEmptyGraph(nodes);

        // 0 --- 1       3 --- 6
        // |   / |       |     |
        // |  /  |       |     |
        // 7     2 - 5   4     9    10
        //
        //       8

        GraphUtils.addUndirectedEdge(graph, 0, 1);
        GraphUtils.addUndirectedEdge(graph, 1, 7);
        GraphUtils.addUndirectedEdge(graph, 7, 0);
        GraphUtils.addUndirectedEdge(graph, 1, 2);
        GraphUtils.addUndirectedEdge(graph, 2, 5);
        GraphUtils.addUndirectedEdge(graph, 3, 4);
        GraphUtils.addUndirectedEdge(graph, 3, 6);
        GraphUtils.addUndirectedEdge(graph, 6, 9);

        final var graphComponents = new ConnectedComponentsDfs(graph);

        Assertions.assertEquals(4, graphComponents.countComponents());
        for (int i = 0; i < nodes; i++) {
            System.out.printf("Node %d -> component %d%n", i, graphComponents.componentId(i));
        }
        // Components: {0,1,2,5,7} label 0, {3,4,6,9} label 1, {8} label 2, {10} label 3
        Assertions.assertArrayEquals(new int[]{0, 0, 0, 1, 1, 0, 1, 0, 2, 1, 3}, graphComponents.getComponentIds());
    }
}
