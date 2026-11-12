package org.example.experiments.dsa.graph;

import java.util.List;

public class DepthFirstSearch {
    private DepthFirstSearch() {
    }

    /**
     * <p>Time: O(V + E)</p>
     * Space: O(V)
     *
     * @param directedGraph as an adjacency list representation
     * @return the number of nodes reachable from {@code startVertex} (including itself).
     */
    public static int dfs(int startVertex, List<List<Integer>> directedGraph) {
        return exploreVertex(startVertex, new boolean[directedGraph.size()], directedGraph);
    }

    private static int exploreVertex(int at, boolean[] visited, List<List<Integer>> graph) {
        if (visited[at]) {
            return 0;
        }
        visited[at] = true;
        int nodeCount = 1;
        for (int to : graph.get(at)) {
            nodeCount += exploreVertex(to, visited, graph);
        }
        return nodeCount;
    }
}
