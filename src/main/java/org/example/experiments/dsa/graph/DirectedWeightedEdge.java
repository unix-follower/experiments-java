package org.example.experiments.dsa.graph;

public record DirectedWeightedEdge(
        GraphVertex from,
        GraphVertex to,
        double weight
) implements GraphEdge<GraphVertex> {
}
