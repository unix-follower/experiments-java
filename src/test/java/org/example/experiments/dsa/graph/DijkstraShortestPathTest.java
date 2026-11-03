package org.example.experiments.dsa.graph;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DijkstraShortestPathTest {
    @Test
    void testDistanceToSingleNode() {
        // given
        final var graph = new AdjacencyListGraph(1);
        final var startVertex = new Vertex(0, "start_vertex");
        graph.addVertex(startVertex);
        final var algorithm = new DijkstraShortestPath(graph);
        // when
        final var path = algorithm.findShortestPath(startVertex, startVertex);
        // then
        Assertions.assertEquals(0.0, path.getTotalWeight());
    }

    @Test
    void testDistanceToSingleNodeWithDenseGraph() {
        // given
        final var graph = new AdjacencyListDenseGraph(1);
        final var algorithm = DijkstraShortestPath.withDenseGraph(graph);
        // when
        final double distance = algorithm.computeDistance(0, 0);
        // then
        Assertions.assertEquals(0.0, distance);
    }

    @Test
    void testTwoNodesDirectEdge() {
        // given
        final var graph = new AdjacencyListGraph(2);
        final var startVertex = new Vertex(0, "start_vertex");
        graph.addVertex(startVertex);
        graph.addVertex(new Vertex(1, "vertex_1"));
        graph.addEdge(startVertex, graph.getVertex(1), 5);
        final var algorithm = new DijkstraShortestPath(graph);
        // when
        final var path = algorithm.findShortestPath(startVertex, startVertex);
        // then
        Assertions.assertEquals(5.0, path.getTotalWeight());
    }

    @Test
    void testTwoNodesDirectEdgeWithDenseGraph() {
        // given
        final var graph = new AdjacencyListDenseGraph(2);
        graph.addEdge(0, 1, 5);
        final var algorithm = DijkstraShortestPath.withDenseGraph(graph);
        // when
        final double distance = algorithm.computeDistance(0, 1);
        // then
        Assertions.assertEquals(5.0, distance);
    }

    @Test
    void testUnreachableNodeReturnsInfinity() {
        // given
        final var graph = new AdjacencyListDenseGraph(3);
        graph.addEdge(0, 1, 5);
        final var algorithm = DijkstraShortestPath.withDenseGraph(graph);
        // when
        final double distance = algorithm.computeDistance(0, 2);
        // then
        Assertions.assertEquals(Double.POSITIVE_INFINITY, distance);
    }

    @Test
    void testShortestPathChosenOverLonger() {
        // given
        final var graph = new AdjacencyListDenseGraph(3);
        graph.addEdge(0, 2, 10);
        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 2, 4);
        final var algorithm = DijkstraShortestPath.withDenseGraph(graph);
        // when
        final double distance = algorithm.computeDistance(0, 2);
        // then
        Assertions.assertEquals(7.0, distance);
    }

    @Test
    void testReconstructPathSimple() {
        // given
        final int numberOfNodes = 4;
        final var graph = new AdjacencyListDenseGraph(numberOfNodes);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);

        final var predecessorTracking = new ShortestPathReconstruction(numberOfNodes);
        final var algorithm = DijkstraShortestPath.withDenseGraph(graph, predecessorTracking);
        algorithm.computeDistance(0, 3);

        // when
        final var reconstructedPath = predecessorTracking.reconstruct(0, 3);
        // then
        Assertions.assertTrue(List.of(0, 1, 2, 3).containsAll(reconstructedPath));
    }

    @Test
    void testReconstructPathChoosesShortest() {
        // given
        final int numberOfNodes = 3;
        final var graph = new AdjacencyListDenseGraph(numberOfNodes);
        graph.addEdge(0, 2, 10);
        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 2, 4);

        final var predecessorTracking = new ShortestPathReconstruction(numberOfNodes);
        final var algorithm = DijkstraShortestPath.withDenseGraph(graph, predecessorTracking);
        algorithm.computeDistance(0, 2);

        // when
        final var reconstructedPath = predecessorTracking.reconstruct(0, 2);
        // then
        Assertions.assertTrue(List.of(0, 1, 2).containsAll(reconstructedPath));
    }

    @Test
    void testReconstructPathUnreachableReturnsEmpty() {
        // given
        final int numberOfNodes = 3;
        final var graph = new AdjacencyListDenseGraph(numberOfNodes);
        graph.addEdge(0, 1, 5);

        final var predecessorTracking = new ShortestPathReconstruction(numberOfNodes);
        final var algorithm = DijkstraShortestPath.withDenseGraph(graph, predecessorTracking);
        algorithm.computeDistance(0, 2);

        // when
        final var reconstructedPath = predecessorTracking.reconstruct(0, 2);
        // then
        Assertions.assertTrue(reconstructedPath.isEmpty());
    }

    @Test
    void testReconstructPathStartEqualsEnd() {
        // given
        final int numberOfNodes = 2;
        final var graph = new AdjacencyListDenseGraph(numberOfNodes);
        graph.addEdge(0, 1, 1);

        final var predecessorTracking = new ShortestPathReconstruction(numberOfNodes);
        final var algorithm = DijkstraShortestPath.withDenseGraph(graph, predecessorTracking);
        algorithm.computeDistance(0, 0);

        // when
        final var reconstructedPath = predecessorTracking.reconstruct(0, 0);
        // then
        Assertions.assertEquals(1, reconstructedPath.size());
        Assertions.assertEquals(0, reconstructedPath.getFirst());
    }

    @Test
    void testDirectedEdgeNotTraversedBackwards() {
        // given
        final var graph = new AdjacencyListDenseGraph(2);
        graph.addEdge(0, 1, 5);
        final var algorithm = DijkstraShortestPath.withDenseGraph(graph);
        // when
        final double distance = algorithm.computeDistance(1, 0);
        // then
        Assertions.assertEquals(Double.POSITIVE_INFINITY, distance);
    }

    @Test
    void testLongerPathWithMoreHops() {
        // given
        final int numberOfNodes = 5;
        final var graph = new AdjacencyListDenseGraph(numberOfNodes);
        // 0→1→2→3→4 with cost 1 each (total 4) vs 0→4 with cost 100
        graph.addEdge(0, 4, 100);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);

        final var predecessorTracking = new ShortestPathReconstruction(numberOfNodes);
        final var algorithm = DijkstraShortestPath.withDenseGraph(graph, predecessorTracking);

        // when
        final double distance = algorithm.computeDistance(0, 4);
        final var reconstructedPath = predecessorTracking.reconstruct(0, 4);
        // then
        Assertions.assertEquals(4.0, distance);
        Assertions.assertTrue(List.of(0, 1, 2, 3, 4).containsAll(reconstructedPath));
    }

    @Test
    void testDisconnectedGraph() {
        // given
        final var graph = new AdjacencyListDenseGraph(4);
        graph.addEdge(0, 1, 3);
        graph.addEdge(2, 3, 1);
        final var algorithm = DijkstraShortestPath.withDenseGraph(graph);
        // when
        final double distance = algorithm.computeDistance(0, 1);
        // then
        Assertions.assertEquals(3.0, distance);
        Assertions.assertEquals(Double.POSITIVE_INFINITY, algorithm.computeDistance(0, 2));
        Assertions.assertEquals(Double.POSITIVE_INFINITY, algorithm.computeDistance(0, 3));
    }
}
