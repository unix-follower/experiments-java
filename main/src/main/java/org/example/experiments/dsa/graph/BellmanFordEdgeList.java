package org.example.experiments.dsa.graph;

import java.util.Arrays;
import java.util.function.ToDoubleFunction;

public class BellmanFordEdgeList {
    private BellmanFordEdgeList() {
    }

    /**
     * @return the array containing the shortest distance to every node
     */
    public static double[] bellmanFord(WeightedEdge[] directedEdges, int numberOfVertices, int startVertex) {
        final double[] distances = new double[numberOfVertices];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[startVertex] = 0;

        // Only in the worst case does it take numberOfVertices-1 iterations for the Bellman-Ford
        // algorithm to complete. Another stopping condition is when we're unable to
        // relax an edge, this means we have reached the optimal solution early.
        relaxEdges(directedEdges, numberOfVertices, distances,
                edge -> distances[edge.from()] + edge.weight());
        // Run algorithm a second time to detect which nodes are part
        // of a negative cycle. A negative cycle has occurred if we
        // can find a better path beyond the optimal solution.
        relaxEdges(directedEdges, numberOfVertices, distances, _ -> Double.NEGATIVE_INFINITY);

        return distances;
    }

    private static void relaxEdges(WeightedEdge[] directedEdges, int numberOfVertices, double[] distances,
                                   ToDoubleFunction<WeightedEdge> toEdgeDistSupplier) {
        boolean relaxedEdge = true;

        for (int vertex = 0; vertex < numberOfVertices - 1 && relaxedEdge; vertex++) {
            relaxedEdge = false;
            for (var edge : directedEdges) {
                if (distances[edge.from()] + edge.weight() < distances[edge.to()]) {
                    distances[edge.to()] = toEdgeDistSupplier.applyAsDouble(edge);
                    relaxedEdge = true;
                }
            }
        }
    }
}
