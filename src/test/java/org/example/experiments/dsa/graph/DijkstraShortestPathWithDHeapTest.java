package org.example.experiments.dsa.graph;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DijkstraShortestPathWithDHeapTest {
    @Test
    void testSingleNode() {
        // given
        final var algorithm = new DijkstraShortestPath.WithDHeap(1);
        // when
        final double distance = algorithm.computeDistance(0, 0);
        // then
        Assertions.assertEquals(0.0, distance);
    }

    @Test
    void testTwoNodesDirectEdge() {
        // given
        final var algorithm = new DijkstraShortestPath.WithDHeap(2);
        algorithm.addEdge(0, 1, 5);
        // then
        final double distance = algorithm.computeDistance(0, 1);
        // when
        Assertions.assertEquals(5.0, distance);
    }

    @Test
    void testUnreachableNodeReturnsInfinity() {
        // given
        final var algorithm = new DijkstraShortestPath.WithDHeap(3);
        algorithm.addEdge(0, 1, 5);
        // when
        final double distance = algorithm.computeDistance(0, 2);
        // then
        Assertions.assertEquals(Double.POSITIVE_INFINITY, distance);
    }

    @Test
    void testShortestPathChosenOverLonger() {
        // given
        final var algorithm = new DijkstraShortestPath.WithDHeap(3);
        algorithm.addEdge(0, 2, 10);
        algorithm.addEdge(0, 1, 3);
        algorithm.addEdge(1, 2, 4);
        // when
        final double distance = algorithm.computeDistance(0, 2);
        // then
        Assertions.assertEquals(7.0, distance);
    }

    @Test
    void testReconstructPathSimple() {
        // given
        final var algorithm = new DijkstraShortestPath.WithDHeap(4);
        algorithm.addEdge(0, 1, 1);
        algorithm.addEdge(1, 2, 1);
        algorithm.addEdge(2, 3, 1);
        // when
        final List<Integer> paths = algorithm.reconstructPath(0, 3);
        // then
        Assertions.assertTrue(List.of(0, 1, 2, 3).containsAll(paths));
    }

    @Test
    void testReconstructPathChoosesShortest() {
        // given
        final var algorithm = new DijkstraShortestPath.WithDHeap(3);
        algorithm.addEdge(0, 2, 10);
        algorithm.addEdge(0, 1, 3);
        algorithm.addEdge(1, 2, 4);
        // when
        final List<Integer> paths = algorithm.reconstructPath(0, 2);
        // then
        Assertions.assertTrue(List.of(0, 1, 2).containsAll(paths));
    }

    @Test
    void testReconstructPathUnreachableReturnsEmpty() {
        // given
        final var algorithm = new DijkstraShortestPath.WithDHeap(3);
        algorithm.addEdge(0, 1, 5);
        // when
        final List<Integer> paths = algorithm.reconstructPath(0, 2);
        // then
        Assertions.assertTrue(paths.isEmpty());
    }

    @Test
    void testReconstructPathStartEqualsEnd() {
        // given
        final var algorithm = new DijkstraShortestPath.WithDHeap(2);
        algorithm.addEdge(0, 1, 1);
        // when
        final List<Integer> paths = algorithm.reconstructPath(0, 0);
        // then
        Assertions.assertEquals(1, paths.size());
        Assertions.assertEquals(0, paths.getFirst());
    }

    @Test
    void testDirectedEdgeNotTraversedBackwards() {
        // given
        final var algorithm = new DijkstraShortestPath.WithDHeap(2);
        algorithm.addEdge(0, 1, 5);
        // when
        final double distance = algorithm.computeDistance(1, 0);
        // then
        Assertions.assertEquals(Double.POSITIVE_INFINITY, distance);
    }

    @Test
    void testLongerPathWithMoreHops() {
        // given
        final var algorithm = new DijkstraShortestPath.WithDHeap(5);
        algorithm.addEdge(0, 4, 100);
        algorithm.addEdge(0, 1, 1);
        algorithm.addEdge(1, 2, 1);
        algorithm.addEdge(2, 3, 1);
        algorithm.addEdge(3, 4, 1);

        // when
        final double distance = algorithm.computeDistance(0, 4);
        final List<Integer> paths = algorithm.reconstructPath(0, 4);

        // then
        Assertions.assertEquals(4.0, distance);
        Assertions.assertTrue(List.of(0, 1, 2, 3, 4).containsAll(paths));
    }

    @Test
    void testDisconnectedGraph() {
        // given
        final var algorithm = new DijkstraShortestPath.WithDHeap(4);
        algorithm.addEdge(0, 1, 3);
        algorithm.addEdge(2, 3, 1);

        // when / then
        Assertions.assertEquals(3.0, algorithm.computeDistance(0, 1));
        Assertions.assertEquals(Double.POSITIVE_INFINITY, algorithm.computeDistance(0, 2));
        Assertions.assertEquals(Double.POSITIVE_INFINITY, algorithm.computeDistance(0, 3));
    }

    @Test
    void testGetGraphReturnsAdjacencyList() {
        // given
        final var algorithm = new DijkstraShortestPath.WithDHeap(3);
        algorithm.addEdge(0, 1, 5);
        algorithm.addEdge(0, 2, 10);

        // when
        final List<List<DirectedWeightedDenseEdge>> graph = algorithm.getGraph();

        // then
        Assertions.assertEquals(3, graph.size());
        Assertions.assertEquals(2, graph.get(0).size());
        Assertions.assertTrue(graph.get(1).isEmpty());
        Assertions.assertTrue(graph.get(2).isEmpty());
    }
}
