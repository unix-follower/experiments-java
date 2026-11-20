package org.example.experiments.dsa.graph;

/**
 * from and to are the array indies.
 */
public record WeightedEdge(
        Integer from,
        Integer to,
        double weight
) implements GraphEdge<Integer> {
    public static WeightedEdge unweighted(Integer from, Integer to) {
        return new WeightedEdge(from, to, 1.0);
    }
}
