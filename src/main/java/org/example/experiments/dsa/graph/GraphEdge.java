package org.example.experiments.dsa.graph;

public interface GraphEdge<V> {
    V to();

    V from();

    double weight();
}
