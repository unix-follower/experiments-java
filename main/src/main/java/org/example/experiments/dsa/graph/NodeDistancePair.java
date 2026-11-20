package org.example.experiments.dsa.graph;

public record NodeDistancePair(GraphVertex vertex, double distance) implements Comparable<NodeDistancePair> {
    @Override
    public int compareTo(NodeDistancePair other) {
        return Double.compare(this.distance, other.distance);
    }
}
