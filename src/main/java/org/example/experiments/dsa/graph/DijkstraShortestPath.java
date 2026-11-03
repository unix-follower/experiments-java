package org.example.experiments.dsa.graph;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class DijkstraShortestPath implements DijkstraAlgorithm<GraphVertex> {
    public static DijkstraAlgorithm<Integer> withDenseGraph(DirectedGraph<Integer, DirectedWeightedDenseEdge> graph) {
        return new WithDenseGraph(graph, null);
    }

    public static DijkstraAlgorithm<Integer> withDenseGraph(
            DirectedGraph<Integer, DirectedWeightedDenseEdge> graph, AlgorithmListener<Integer> listener) {
        return new WithDenseGraph(graph, listener);
    }

    private final int numberOfNodes;
    private double[] distances;
    private final Graph<GraphVertex, DirectedWeightedEdge> graph;
    private VertexPath vertexPath;

    public DijkstraShortestPath(Graph<GraphVertex, DirectedWeightedEdge> graph) {
        this.numberOfNodes = graph.getVertices().size();
        this.graph = graph;
    }

    @Override
    public double computeDistance(GraphVertex startVertex, GraphVertex targetVertex) {
        // TODO: implement
        return 0;
    }

    @Override
    public GraphPath<GraphVertex> findShortestPath(GraphVertex sourceVertex, GraphVertex targetVertex) {
        distances = new double[numberOfNodes];
        // Assume that all nodes are unreachable from the source vertex
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        // set distance to the source vertex to 0
        distances[sourceVertex.nodeId()] = 0;

        vertexPath = new VertexPath();

        final var priorityQueue = new PriorityQueue<NodeDistancePair>();
        priorityQueue.offer(new NodeDistancePair(sourceVertex, 0));

        final boolean[] visited = new boolean[numberOfNodes];

        while (!priorityQueue.isEmpty()) {
            final var nodeDistancePair = priorityQueue.poll();
            final var node = nodeDistancePair.vertex();
            final int nodeId = node.nodeId();
            visited[nodeId] = true;

            if (distances[nodeId] < nodeDistancePair.distance()) {
                // The cheaper path is already found
                continue;
            }

            for (DirectedWeightedEdge edge : graph.getEdges(nodeId)) {
                if (visited[edge.to().nodeId()]) {
                    continue; // Skip visited node
                }

                // Minimizing distances
                relaxEdge(edge, priorityQueue, nodeId);
            }

            // Return early because the distance cannot be improved further.
            if (nodeId == targetVertex.nodeId()) {
                return vertexPath;
            }
        }
        // No path between the starting node and the destination node
        vertexPath.setTotalWeight(Double.POSITIVE_INFINITY);
        return vertexPath;
    }

    private void relaxEdge(DirectedWeightedEdge edge, Queue<NodeDistancePair> priorityQueue, int nodeId) {
        final double distanceToNextVertex = distances[edge.from().nodeId()] + edge.weight();
        if (distanceToNextVertex < distances[edge.to().nodeId()]) {
            distances[edge.to().nodeId()] = distanceToNextVertex;
            vertexPath.setTotalWeight(distanceToNextVertex);
            final var vertex = new Vertex(edge.to().nodeId(), "vertex_" + nodeId);
            vertexPath.addVertex(vertex);
            priorityQueue.offer(new NodeDistancePair(vertex, distances[edge.to().nodeId()]));
        }
    }

    static class WithDenseGraph implements DijkstraAlgorithm<Integer> {
        private final int numberOfNodes;
        private double[] distances;
        private final DirectedGraph<Integer, DirectedWeightedDenseEdge> graph;
        private final AlgorithmListener<Integer> listener;

        public WithDenseGraph(
                DirectedGraph<Integer, DirectedWeightedDenseEdge> graph,
                AlgorithmListener<Integer> listener) {
            this.numberOfNodes = graph.getVertices().size();
            this.graph = graph;
            this.listener = listener != null ? listener : (_, _, _) -> {
            };
        }

        @Override
        public GraphPath<Integer> findShortestPath(Integer sourceVertex, Integer targetVertex) {
            // TODO: implement
            return null;
        }

        @Override
        public double computeDistance(Integer startVertex, Integer targetVertex) {
            distances = new double[numberOfNodes];
            // Assume that all nodes are unreachable from the source vertex
            Arrays.fill(distances, Double.POSITIVE_INFINITY);
            // set distance to the source vertex to 0
            distances[startVertex] = 0;

            final var priorityQueue = new PriorityQueue<NodeDistancePair>();
            priorityQueue.offer(new NodeDistancePair(new Vertex(startVertex, "start_vertex"), 0));

            final boolean[] visited = new boolean[numberOfNodes];

            while (!priorityQueue.isEmpty()) {
                final var nodeDistancePair = priorityQueue.poll();
                final var node = nodeDistancePair.vertex();
                final int nodeId = node.nodeId();
                visited[nodeId] = true;

                if (distances[nodeId] < nodeDistancePair.distance()) {
                    // The cheaper path is already found
                    continue;
                }

                for (DirectedWeightedDenseEdge edge : graph.getEdges(nodeId)) {
                    if (visited[edge.to()]) {
                        continue; // Skip visited node
                    }

                    // Minimizing distances
                    relaxEdge(edge, priorityQueue, nodeId);
                }

                // Return early because the distance cannot be improved further.
                if (nodeId == targetVertex) {
                    return distances[targetVertex];
                }
            }
            // No path between the starting node and the destination node
            return Double.POSITIVE_INFINITY;
        }

        private void relaxEdge(DirectedWeightedDenseEdge edge, Queue<NodeDistancePair> priorityQueue, int nodeId) {
            final double distanceToNextVertex = distances[edge.from()] + edge.weight();
            if (distanceToNextVertex < distances[edge.to()]) {
                listener.onEdgeRelaxed(nodeId, edge.from(), distanceToNextVertex);
                distances[edge.to()] = distanceToNextVertex;
                final var vertex = new Vertex(edge.to(), "vertex_" + nodeId);
                priorityQueue.offer(new NodeDistancePair(vertex, distances[edge.to()]));
            }
        }
    }
}
