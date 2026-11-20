package org.example.experiments.dsa.graph;

public interface UndirectedGraph<V, E> extends Graph<V, E> {
    default boolean isDirected() { return false; }
}
