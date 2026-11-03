package org.example.experiments.dsa.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class AdjacencyListDenseGraph implements DirectedGraph<Integer, DirectedWeightedDenseEdge> {
    private final List<List<DirectedWeightedDenseEdge>> graph;

    public AdjacencyListDenseGraph(int numberOfNodes) {
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
    public Collection<DirectedWeightedDenseEdge> getEdges(int nodeId) {
        return graph.get(nodeId);
    }

    @Override
    public void addVertex(Integer vertex) {
        graph.add(vertex, new ArrayList<>());
    }

    @Override
    public DirectedWeightedDenseEdge addEdge(Integer from, Integer to, double weight) {
        var edge = new DirectedWeightedDenseEdge(from, to, weight);
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
    public Collection<DirectedWeightedDenseEdge> getEdges() {
        // TODO: implement
        return List.of();
    }

    @Override
    public Collection<Integer> getNeighbors(Integer vertex) {
        // TODO: implement
        return List.of();
    }
}
