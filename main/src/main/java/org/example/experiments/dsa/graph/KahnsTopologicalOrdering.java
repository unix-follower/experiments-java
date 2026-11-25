package org.example.experiments.dsa.graph;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class KahnsTopologicalOrdering {
    private KahnsTopologicalOrdering() {
    }

    /**
     * Time complexity: O(V+E)
     */
    public static int[] topologicalOrder(List<List<Integer>> acyclicGraph) {
        final int nodes = acyclicGraph.size();

        // Calculate the in-degree of each node.
        final int[] inDegree = new int[nodes];
        for (List<Integer> edges : acyclicGraph) {
            for (int to : edges) {
                inDegree[to]++;
            }
        }

        // Contains the set nodes with no incoming edges.
        final Queue<Integer> loneVertices = new ArrayDeque<>();

        // Find all start nodes.
        for (int i = 0; i < nodes; i++) {
            if (inDegree[i] == 0) {
                loneVertices.offer(i);
            }
        }

        int index = 0;
        final int[] order = new int[nodes];
        while (!loneVertices.isEmpty()) {
            final int at = loneVertices.poll();
            order[index++] = at;
            for (int to : acyclicGraph.get(at)) {
                inDegree[to]--;
                if (inDegree[to] == 0) {
                    loneVertices.offer(to);
                }
            }
        }
        if (index != nodes) {
            throw new IllegalArgumentException("Graph is not acyclic! Detected a cycle.");
        }
        return order;
    }
}
