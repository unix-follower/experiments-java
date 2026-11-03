package org.example.experiments.dsa.graph;

public record DirectedWeightedDenseEdge(
        Integer from,
        Integer to,
        double weight
) implements GraphEdge<Integer> {
}
