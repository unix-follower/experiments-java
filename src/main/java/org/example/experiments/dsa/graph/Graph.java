package org.example.experiments.dsa.graph;

import java.util.Collection;

public interface Graph<V, E> {
    V getVertex(int nodeId);
    Collection<E> getEdges(int nodeId);

    void addVertex(V vertex);

    E addEdge(V from, V to, double weight);

    /**
     * Defaults to 1 for unweighted edges.
     */
    default E addEdge(V from, V to) {
        return addEdge(from, to, 1.0);
    }

    Collection<V> getVertices();

    Collection<E> getEdges();

    Collection<V> getNeighbors(V vertex);

    boolean isDirected();
}
