package org.example.experiments.dsa.graph;

public interface DijkstraAlgorithm<V> extends ShortestPathAlgorithm<V> {
    double computeDistance(V startVertex, V targetVertex);
}
