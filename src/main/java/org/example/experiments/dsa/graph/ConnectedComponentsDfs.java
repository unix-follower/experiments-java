package org.example.experiments.dsa.graph;

import java.util.Arrays;
import java.util.List;

/**
 * <p>For directed graphs, use Tarjan's or Kosaraju's algorithm
 * to find <em>strongly</em> connected components instead.
 */
public class ConnectedComponentsDfs {
    private final int numberOfVertices;
    private final List<List<Integer>> undirectedGraph;
    private boolean graphTraversed;
    private int componentCount;
    private int[] componentIds;

    public ConnectedComponentsDfs(List<List<Integer>> undirectedGraph) {
        if (undirectedGraph == null) {
            throw new IllegalArgumentException();
        }
        this.numberOfVertices = undirectedGraph.size();
        this.undirectedGraph = undirectedGraph;
    }

    /**
     * @return the number of connected components.
     */
    public int countComponents() {
        exploreGraph();
        return componentCount;
    }

    /**
     * @return the component id of the given node. Component ids are 0-indexed.
     */
    public int componentId(int node) {
        exploreGraph();
        return componentIds[node];
    }

    /**
     * @return the component id array where {@code componentIds[i]} is the
     * component id of node i. Component ids are 0-indexed.
     */
    public int[] getComponentIds() {
        exploreGraph();
        return componentIds;
    }

    /**
     * <p>Time: O(V + E)</p>
     * Space: O(V)
     */
    private void exploreGraph() {
        if (graphTraversed) {
            return;
        }

        componentIds = new int[numberOfVertices];
        Arrays.fill(componentIds, -1);

        for (int i = 0; i < numberOfVertices; i++) {
            if (componentIds[i] == -1) {
                // Each unvisited node starts a new DFS
                // that labels every reachable node with the same component id.
                dfs(i, componentCount++);
            }
        }

        graphTraversed = true;
    }

    private void dfs(int at, int componentId) {
        componentIds[at] = componentId;
        for (int to : undirectedGraph.get(at)) {
            if (componentIds[to] == -1) {
                dfs(to, componentId);
            }
        }
    }
}
