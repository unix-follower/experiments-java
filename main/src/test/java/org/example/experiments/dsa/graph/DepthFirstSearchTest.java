package org.example.experiments.dsa.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.example.experiments.dsa.graph.DepthFirstSearch.dfs;

class DepthFirstSearchTest {
    private static List<List<Integer>> createGraph(int nodes) {
        final List<List<Integer>> adjacencyListGraph = new ArrayList<>(nodes);
        for (int i = 0; i < nodes; i++) {
            adjacencyListGraph.add(new ArrayList<>());
        }
        return adjacencyListGraph;
    }

    private static void addDirectedEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to);
    }

    @Test
    void testReachability() {
        // given
        final int nodes = 7;
        final List<List<Integer>> graph = createGraph(nodes);

        // 0 ---> 1 ---> 3
        // |      |
        // v      v
        // 2      4 ---> 5    6
        addDirectedEdge(graph, 0, 1);
        addDirectedEdge(graph, 0, 2);
        addDirectedEdge(graph, 1, 3);
        addDirectedEdge(graph, 1, 4);
        addDirectedEdge(graph, 4, 5);

        // when
        final int nodeCountReachableFrom0 = dfs(0, graph);
        final int nodeCountReachableFrom6 = dfs(6, graph);
        // then
        Assertions.assertEquals(6, nodeCountReachableFrom0);
        Assertions.assertEquals(1, nodeCountReachableFrom6);
    }
}
