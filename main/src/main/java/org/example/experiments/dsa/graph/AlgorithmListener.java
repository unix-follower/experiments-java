package org.example.experiments.dsa.graph;

@FunctionalInterface
public interface AlgorithmListener<V> {
    void onEdgeRelaxed(V to, V from, double newDistance);
}
