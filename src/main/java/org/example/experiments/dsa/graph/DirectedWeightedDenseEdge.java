package org.example.experiments.dsa.graph;

/**
 * from and to are the array indies.
 */
public record DirectedWeightedDenseEdge(
        Integer from,
        Integer to,
        double weight
) implements GraphEdge<Integer> {
}
