package org.example.experiments.dsa.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.example.experiments.dsa.graph.DepthFirstSearch.dfs;

class DepthFirstSearchTest {
    @Test
    void testReachability() {
        // given
        final int nodes = 7;
        final List<List<Integer>> graph = GraphUtils.createEmptyGraph(nodes);

        // 0 ---> 1 ---> 3
        // |      |
        // v      v
        // 2      4 ---> 5    6
        GraphUtils.addDirectedEdge(graph, 0, 1);
        GraphUtils.addDirectedEdge(graph, 0, 2);
        GraphUtils.addDirectedEdge(graph, 1, 3);
        GraphUtils.addDirectedEdge(graph, 1, 4);
        GraphUtils.addDirectedEdge(graph, 4, 5);

        // when
        final int nodeCountReachableFrom0 = dfs(0, graph);
        final int nodeCountReachableFrom6 = dfs(6, graph);
        // then
        Assertions.assertEquals(6, nodeCountReachableFrom0);
        Assertions.assertEquals(1, nodeCountReachableFrom6);
    }
}
