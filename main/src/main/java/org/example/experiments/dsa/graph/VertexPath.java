package org.example.experiments.dsa.graph;

import java.util.ArrayList;
import java.util.List;

public class VertexPath implements GraphPath<GraphVertex> {
    private final List<GraphVertex> vertices = new ArrayList<>();
    private double totalWeight;

    @Override
    public boolean hasRepeatedVertices() {
        // TODO: implement
        return false;
    }

    @Override
    public boolean hasRepeatedEdges() {
        // TODO: implement
        return false;
    }

    @Override
    public List<GraphVertex> getVertices() {
        return vertices;
    }

    @Override
    public double getTotalWeight() {
        return totalWeight;
    }

    @Override
    public boolean containsVertex(GraphVertex vertex) {
        // TODO: implement
        return false;
    }

    public void addVertex(GraphVertex vertex) {
        vertices.add(vertex);
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }
}
