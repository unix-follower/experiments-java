package org.example.experiments.dsa.graph;

public record DirectedWeightedGraphEdge(
        GraphVertex from,
        GraphVertex to,
        double weight
) implements GraphEdge<GraphVertex> {
}
