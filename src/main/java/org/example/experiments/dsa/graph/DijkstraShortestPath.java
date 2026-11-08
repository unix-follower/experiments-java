package org.example.experiments.dsa.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

import static java.lang.Math.max;
import static java.lang.Math.min;

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

    private static class WithDenseGraph implements DijkstraAlgorithm<Integer> {
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

    static class WithDHeap implements DijkstraAlgorithm<Integer> {
        private final int numberOfNodes;

        private int edgeCount;
        private double[] distances;
        private Integer[] predecessors;
        private final List<List<DirectedWeightedDenseEdge>> graph;

        public WithDHeap(int numberOfNodes) {
            this.numberOfNodes = numberOfNodes;
            this.graph = new ArrayList<>(numberOfNodes);
            for (int i = 0; i < numberOfNodes; i++) {
                graph.add(new ArrayList<>());
            }
        }

        public void addEdge(int from, int to, int cost) {
            edgeCount++;
            graph.get(from).add(new DirectedWeightedDenseEdge(from, to, cost));
        }

        public List<List<DirectedWeightedDenseEdge>> getGraph() {
            return graph;
        }

        /**
         * Relax edge by updating minimum cost if applicable
         */
        private void relaxEdge(DirectedWeightedDenseEdge edge, int nodeId, MinIndexedDHeap<Double> ipq) {
            final double newDistance = distances[nodeId] + edge.weight();
            if (newDistance < distances[edge.to()]) {
                predecessors[edge.to()] = nodeId;
                distances[edge.to()] = newDistance;
                // Insert the cost of going to a node for the first time in the PQ,
                // or try updating it to a better value by calling decrease.
                if (!ipq.contains(edge.to())) {
                    ipq.insert(edge.to(), newDistance);
                } else {
                    ipq.decrease(edge.to(), newDistance);
                }
            }
        }

        /**
         * Reconstructs the shortest path (of nodes) from 'start' to 'end' inclusive.
         *
         * @return An array of node indexes of the shortest path from 'start' to 'end'. If 'start' and
         * 'end' are not connected then an empty list is returned.
         */
        public List<Integer> reconstructPath(int start, int end) {
            computeDistance(start, end);
            final var path = new LinkedList<Integer>();
            if (distances[end] == Double.POSITIVE_INFINITY) {
                return path;
            }
            for (int at = end; at != start; at = predecessors[at]) {
                path.addFirst(at);
            }
            path.addFirst(start);
            return path;
        }

        @Override
        public double computeDistance(Integer startVertex, Integer targetVertex) {
            // Keep an Indexed Priority Queue (IPQ) of the next most promising node to visit
            final int degree = edgeCount / numberOfNodes;
            final var ipq = new MinIndexedDHeap<Double>(degree, numberOfNodes);
            ipq.insert(startVertex, 0.0);

            distances = new double[numberOfNodes];
            // Assume that all nodes are unreachable from the source vertex
            Arrays.fill(distances, Double.POSITIVE_INFINITY);
            // Set distance to the source vertex to 0
            distances[startVertex] = 0.0;

            final boolean[] visited = new boolean[numberOfNodes];
            predecessors = new Integer[numberOfNodes];

            while (!ipq.isEmpty()) {
                final int nodeId = ipq.peekMinKeyIndex();

                visited[nodeId] = true;
                final double minValue = ipq.pollMinValue();

                // We already found a better path before the processing of this node
                if (minValue > distances[nodeId]) {
                    continue;
                }

                for (var edge : graph.get(nodeId)) {
                    if (visited[edge.to()]) {
                        continue;
                    }

                    relaxEdge(edge, nodeId, ipq);
                }

                // Once we've processed the end node we can return early because the
                // distance cannot improve after this point.
                if (nodeId == targetVertex) {
                    return distances[targetVertex];
                }
            }
            // No path between the starting node and the destination node
            return Double.POSITIVE_INFINITY;
        }

        @Override
        public GraphPath<Integer> findShortestPath(Integer sourceVertex, Integer targetVertex) {
            throw new UnsupportedOperationException();
        }

        /**
         * A D-ary heap
         */
        private static class MinIndexedDHeap<T extends Comparable<T>> {
            private final int elementsMaxNumber;
            private final int nodeDegree;
            private final int[] lookupChildIndexes;
            private final int[] lookupParentIndexes;

            private int size;

            /**
             * Maps Key Indexes (KI) to where the position of that
             * key is represented in the priority queue in the domain [0, size).
             */
            public final int[] positionMap;

            /**
             * Stores the indexes of the keys in the range [0, size) which makes up the priority queue.
             * inverseMap and positionMap are inverses of each other,
             * so: positionMap[inverseMap[i]] = inverseMap[positionMap[i]] = i
             */
            public final int[] inverseMap;

            /**
             * The values associated with the keys.This array is indexed by the key indexes (KI).
             */
            public final Object[] values;

            public MinIndexedDHeap(int degree, int maxSize) {
                if (maxSize <= 0) {
                    throw new IllegalArgumentException("maxSize <= 0");
                }

                this.nodeDegree = max(2, degree);
                elementsMaxNumber = max(this.nodeDegree + 1, maxSize);

                inverseMap = new int[elementsMaxNumber];
                positionMap = new int[elementsMaxNumber];
                lookupChildIndexes = new int[elementsMaxNumber];
                lookupParentIndexes = new int[elementsMaxNumber];
                values = new Object[elementsMaxNumber];

                for (int i = 0; i < elementsMaxNumber; i++) {
                    lookupParentIndexes[i] = (i - 1) / this.nodeDegree;
                    lookupChildIndexes[i] = i * this.nodeDegree + 1;
                    positionMap[i] = inverseMap[i] = -1;
                }
            }

            public int size() {
                return size;
            }

            public boolean isEmpty() {
                return size == 0;
            }

            public boolean contains(int ki) {
                keyInBoundsOrThrow(ki);
                return positionMap[ki] != -1;
            }

            public int peekMinKeyIndex() {
                isNotEmptyOrThrow();
                return inverseMap[0];
            }

            public int pollMinKeyIndex() {
                final int minKI = peekMinKeyIndex();
                delete(minKI);
                return minKI;
            }

            @SuppressWarnings("unchecked")
            public T peekMinValue() {
                isNotEmptyOrThrow();
                return (T) values[inverseMap[0]];
            }

            public T pollMinValue() {
                final T minValue = peekMinValue();
                delete(peekMinKeyIndex());
                return minValue;
            }

            public void insert(int ki, T value) {
                if (contains(ki)) {
                    throw new IllegalArgumentException("index already exists; received: " + ki);
                }
                Objects.requireNonNull(value);
                positionMap[ki] = size;
                inverseMap[size] = ki;
                values[ki] = value;
                swim(size++);
            }

            @SuppressWarnings("unchecked")
            public T valueOf(int ki) {
                keyExistsOrThrow(ki);
                return (T) values[ki];
            }

            @SuppressWarnings("unchecked")
            public T delete(int ki) {
                keyExistsOrThrow(ki);
                final int i = positionMap[ki];
                swap(i, --size);
                sink(i);
                swim(i);
                T value = (T) values[ki];
                values[ki] = null;
                positionMap[ki] = -1;
                inverseMap[size] = -1;
                return value;
            }

            @SuppressWarnings("unchecked")
            public T update(int ki, T value) {
                keyExistsAndValueNotNullOrThrow(ki, value);
                final int i = positionMap[ki];
                T oldValue = (T) values[ki];
                values[ki] = value;
                sink(i);
                swim(i);
                return oldValue;
            }

            /**
             * Strictly decreases the value associated with KI to 'value'
             */
            public void decrease(int ki, T value) {
                keyExistsAndValueNotNullOrThrow(ki, value);
                if (less(value, values[ki])) {
                    values[ki] = value;
                    swim(positionMap[ki]);
                }
            }

            /**
             * Strictly increases the value associated with KI to 'value'
             */
            public void increase(int ki, T value) {
                keyExistsAndValueNotNullOrThrow(ki, value);
                if (less(values[ki], value)) {
                    values[ki] = value;
                    sink(positionMap[ki]);
                }
            }

            private void sink(int index) {
                int i = index;
                for (int j = minChild(i); j != -1; ) {
                    swap(i, j);
                    i = j;
                    j = minChild(i);
                }
            }

            private void swim(int index) {
                int i = index;
                while (less(i, lookupParentIndexes[i])) {
                    swap(i, lookupParentIndexes[i]);
                    i = lookupParentIndexes[i];
                }
            }

            /**
             * From the parent node at index i find the minimum child below it
             */
            private int minChild(int parentNodeIndex) {
                int i = parentNodeIndex;
                int index = -1;
                final int from = lookupChildIndexes[i];
                final int to = min(size, from + nodeDegree);
                for (int j = from; j < to; j++) {
                    if (less(j, i)) {
                        index = i = j;
                    }
                }
                return index;
            }

            private void swap(int i, int j) {
                positionMap[inverseMap[j]] = i;
                positionMap[inverseMap[i]] = j;
                final int tmp = inverseMap[i];
                inverseMap[i] = inverseMap[j];
                inverseMap[j] = tmp;
            }

            /**
             * Tests if the value of node i < node j
             */
            @SuppressWarnings("unchecked")
            private boolean less(int i, int j) {
                return ((Comparable<? super T>) values[inverseMap[i]]).compareTo((T) values[inverseMap[j]]) < 0;
            }

            @SuppressWarnings("unchecked")
            private boolean less(Object obj1, Object obj2) {
                return ((Comparable<? super T>) obj1).compareTo((T) obj2) < 0;
            }

            @Override
            public String toString() {
                final var inverses = new ArrayList<Integer>(size);
                for (int i = 0; i < size; i++) {
                    inverses.add(inverseMap[i]);
                }
                return inverses.toString();
            }

            private void isNotEmptyOrThrow() {
                if (isEmpty()) {
                    throw new NoSuchElementException("Priority queue underflow");
                }
            }

            private void keyExistsAndValueNotNullOrThrow(int ki, Object value) {
                keyExistsOrThrow(ki);
                Objects.requireNonNull(value);
            }

            private void keyExistsOrThrow(int ki) {
                if (!contains(ki)) {
                    throw new NoSuchElementException("Index does not exist; received: " + ki);
                }
            }

            private void keyInBoundsOrThrow(int ki) {
                if (ki < 0 || ki >= elementsMaxNumber) {
                    throw new IllegalArgumentException("Key index out of bounds; received: " + ki);
                }
            }
        }
    }
}
