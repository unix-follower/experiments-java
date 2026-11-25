package org.example.experiments.dsa.graph;

import java.util.List;
import java.util.Random;

public class DagGenerator {
    private final double edgeProbability;
    private final int minLevels;
    private final int maxLevels;
    private final int minNodesPerLevel;
    private final int maxNodesPerLevel;
    private final Random random;

    /**
     * @param edgeProbability Make sure it is high enough to make a mostly connected graph.
     */
    public DagGenerator(
            int minLevels,
            int maxLevels,
            int minNodesPerLevel,
            int maxNodesPerLevel,
            double edgeProbability) {
        this.minLevels = minLevels;
        this.maxLevels = maxLevels;
        this.minNodesPerLevel = minNodesPerLevel;
        this.maxNodesPerLevel = maxNodesPerLevel;
        this.edgeProbability = edgeProbability;
        random = new Random(42);
    }

    private int rand(int min, int max) {
        return min + (int) (random.nextDouble(1) * (max - min + 1));
    }

    public List<List<Integer>> createDag() {
        final int levels = rand(minLevels, maxLevels);
        final int[] nodesPerLevel = new int[levels];

        int nodes = 0;
        for (int l = 0; l < levels; l++) {
            nodesPerLevel[l] = rand(minNodesPerLevel, maxNodesPerLevel);
            nodes += nodesPerLevel[l];
        }
        final List<List<Integer>> graph = GraphUtils.createEmptyGraph(nodes);
        int levelIndex = 0;
        for (int level = 0; level < levels - 1; level++) {
            for (int i = 0; i < nodesPerLevel[level]; i++) { // For each node on each level
                for (int j = 0; j < nodesPerLevel[level + 1]; j++) { // For each possible edge link
                    if (Math.random() <= edgeProbability) {
                        GraphUtils.addDirectedEdge(graph, levelIndex + i, levelIndex + nodesPerLevel[level] + j);
                    }
                }
            }
            levelIndex += nodesPerLevel[level];
        }
        return graph;
    }
}
