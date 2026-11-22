package org.example.experiments.dsa.graph;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>NOTE: An alternative to topological sorting is Kahn's algorithm, which uses
 * in-degree counting and BFS instead of DFS. See {@link Kahns}.</p>
 */
public class TopologicalSort {
    private TopologicalSort() {
    }

    /**
     * Finds a topological ordering of the nodes in a DAG.
     * For every directed edge u -> v, u comes before v in the ordering.
     * <p>NOTE: {@code numNodes} is not necessarily the number of nodes in the adjacency list since
     * singleton nodes with no edges wouldn't be present but are still part of the graph.</p>
     * <p>Time: O(V + E)</p>
     * Space: O(V + E)
     *
     * @param graph an adjacency list mapping node index to outgoing edges.
     * @return array of node indexes in a (non-unique) topological order.
     */
    public static int[] topologicalSort(Map<Integer, List<WeightedEdge>> graph, int numNodes) {
        final int[] ordering = new int[numNodes];
        final boolean[] visited = new boolean[numNodes];

        int nodeIndex = numNodes - 1;
        for (int at = 0; at < numNodes; at++) {
            if (!visited[at]) {
                nodeIndex = dfs(nodeIndex, at, visited, ordering, graph);
            }
        }

        return ordering;
    }

    /**
     * Places nodes into the ordering array in reverse DFS post-order.
     */
    private static int dfs(
            int nodeIndex, int at, boolean[] visited, int[] ordering, Map<Integer, List<WeightedEdge>> graph) {
        visited[at] = true;

        int i = nodeIndex;
        final List<WeightedEdge> edges = graph.get(at);
        if (edges != null) {
            for (var edge : edges) {
                if (!visited[edge.to()]) {
                    i = dfs(i, edge.to(), visited, ordering, graph);
                }
            }
        }

        ordering[i] = at;
        return i - 1;
    }

    /**
     * Finds the shortest path from {@code startVertex} to all other reachable nodes in a DAG.
     *
     * <p>NOTE: {@code numNodes} is not necessarily the number of nodes in the adjacency list since
     * singleton nodes with no edges wouldn't be present but are still part of the graph.</p>
     *
     * @param graph       adjacency list mapping node index to outgoing edges.
     * @param startVertex the source node.
     * @return array of shortest distances, null for unreachable nodes.
     */
    public static Double[] dagShortestPath(
            Map<Integer, List<WeightedEdge>> graph, int startVertex, int numNodes) {
        final int[] topsort = topologicalSort(graph, numNodes);
        final Double[] distances = new Double[numNodes];
        distances[startVertex] = 0.;

        for (int i = 0; i < numNodes; i++) {
            final int nodeIndex = topsort[i];
            if (distances[nodeIndex] == null) {
                continue;
            }
            for (var edge : graph.getOrDefault(nodeIndex, Collections.emptyList())) {
                final double newDistance = distances[nodeIndex] + edge.weight();
                if (distances[edge.to()] == null || newDistance < distances[edge.to()]) {
                    distances[edge.to()] = newDistance;
                }
            }
        }

        return distances;
    }
}
