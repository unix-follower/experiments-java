package org.example.experiments.dsa.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class AdjacencyListIndexGraph implements DirectedGraph<Integer, WeightedEdge> {
    private final List<List<WeightedEdge>> graph;

    public AdjacencyListIndexGraph(int numberOfNodes) {
        this.graph = new ArrayList<>(numberOfNodes);
        for (int i = 0; i < numberOfNodes; i++) {
            graph.add(new ArrayList<>());
        }
    }

    @Override
    public Integer getVertex(int nodeId) {
        if (nodeId > graph.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return nodeId;
    }

    @Override
    public Collection<WeightedEdge> getEdges(int nodeId) {
        return graph.get(nodeId);
    }

    @Override
    public void addVertex(Integer vertex) {
        graph.add(vertex, new ArrayList<>());
    }

    @Override
    public WeightedEdge addEdge(Integer from, Integer to, double weight) {
        final var edge = new WeightedEdge(from, to, weight);
        graph.get(from).add(edge);
        return edge;
    }

    @Override
    public Collection<Integer> getVertices() {
        return IntStream.iterate(0, i -> i + 1)
                .limit(graph.size())
                .boxed()
                .toList();
    }

    @Override
    public Collection<WeightedEdge> getEdges() {
        // TODO: implement
        return List.of();
    }

    @Override
    public Collection<Integer> getNeighbors(Integer vertex) {
        // TODO: implement
        return List.of();
    }
}
