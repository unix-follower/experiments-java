package org.example.experiments.dsa.graph;

/**
 * <ul>
 *     <li>1 vertex (1-to-1): this happens when the source and target are the exact same vertex with
 *     a total weight of 0.0.</ol>
 *     <li>0 vertices (No Path): if the target vertex is unreachable from the source
 *     (e.g., in a disconnected graph).</ol>
 *     <li>Many vertices: a multi-step route.</ol>
 * </ul>
 */
public interface ShortestPathAlgorithm<V> {
    GraphPath<V> findShortestPath(V sourceVertex, V targetVertex);
}
