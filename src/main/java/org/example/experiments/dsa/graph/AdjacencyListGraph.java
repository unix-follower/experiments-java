package org.example.experiments.dsa.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdjacencyListGraph implements DirectedGraph<GraphVertex, DirectedWeightedEdge> {
    private final Map<GraphVertex, List<DirectedWeightedEdge>> adjacencyList;
    private final Map<Integer, GraphVertex> vertexByNodeIdLookupTable;
    private final Set<GraphVertex> vertices;

    public AdjacencyListGraph(int numberOfNodes) {
        adjacencyList = HashMap.newHashMap(numberOfNodes);
        vertexByNodeIdLookupTable = HashMap.newHashMap(numberOfNodes);
        vertices = HashSet.newHashSet(numberOfNodes);
    }

    @Override
    public GraphVertex getVertex(int nodeId) {
        return vertexByNodeIdLookupTable.get(nodeId);
    }

    @Override
    public Collection<DirectedWeightedEdge> getEdges(int nodeId) {
        return adjacencyList.get(getVertex(nodeId));
    }

    @Override
    public void addVertex(GraphVertex vertex) {
        vertices.add(vertex);
        vertexByNodeIdLookupTable.put(vertex.nodeId(), vertex);
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    @Override
    public DirectedWeightedEdge addEdge(GraphVertex from, GraphVertex to, double weight) {
        final var edge = new DirectedWeightedEdge(from, to, weight);
        adjacencyList.get(from).add(edge);
        return edge;
    }

    @Override
    public Collection<GraphVertex> getVertices() {
        return vertices;
    }

    @Override
    public Collection<DirectedWeightedEdge> getEdges() {
        // TODO: implement
        return List.of();
    }

    @Override
    public Collection<GraphVertex> getNeighbors(GraphVertex vertex) {
        // TODO: implement
        return List.of();
    }
}
