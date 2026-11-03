package org.example.experiments.dsa.graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ShortestPathReconstruction implements AlgorithmListener<Integer> {
    private final double[] distances;
    private final Integer[] predecessors;

    public ShortestPathReconstruction(int numberOfNodes) {
        distances = new double[numberOfNodes];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        predecessors = new Integer[numberOfNodes];
    }

    @Override
    public void onEdgeRelaxed(Integer to, Integer from, double newDistance) {
        predecessors[to] = from;
        distances[to] = newDistance;
    }

    public List<Integer> reconstruct(int startVertex, int targetVertex) {
        distances[startVertex] = 0;

        final List<Integer> path = new LinkedList<>();
        if (distances[targetVertex] == Double.POSITIVE_INFINITY) {
            // start and end are disconnected
            return path;
        }
        for (int vertexAt = targetVertex; vertexAt != startVertex; vertexAt = predecessors[vertexAt]) {
            path.addFirst(vertexAt);
        }
        path.addFirst(startVertex);
        return path;
    }
}
