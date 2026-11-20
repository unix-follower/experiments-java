package org.example.experiments.dsa.graph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * <p>Time: O(V + E)</p>
 * Space: O(V)
 */
public class BreadthFirstSearchAlgorithm {
    private final int totalVertices;
    private final List<List<WeightedEdge>> graph;
    private Integer[] predecessors;

    public BreadthFirstSearchAlgorithm(List<List<WeightedEdge>> graph) {
        Objects.requireNonNull(graph);
        this.totalVertices = graph.size();
        this.graph = graph;
    }

    /**
     * @return node indices along the shortest path.
     */
    public List<Integer> reconstructPath(int startVertex, int targetVertex) {
        bfs(startVertex);
        final LinkedList<Integer> path = new LinkedList<>();
        for (Integer at = targetVertex; at != null; at = predecessors[at]) {
            path.addFirst(at);
        }
        if (path.isEmpty() || path.getFirst() != startVertex) {
            return List.of(); // Unreachable
        }
        return path;
    }

    private void bfs(int startVertex) {
        predecessors = new Integer[totalVertices];
        final boolean[] visited = new boolean[totalVertices];
        final Deque<Integer> queue = new ArrayDeque<>(totalVertices);

        queue.offer(startVertex);
        visited[startVertex] = true;

        while (!queue.isEmpty()) {
            final int node = queue.poll();
            for (var edge : graph.get(node)) {
                if (!visited[edge.to()]) {
                    visited[edge.to()] = true;
                    predecessors[edge.to()] = node;
                    queue.offer(edge.to());
                }
            }
        }
    }
}
