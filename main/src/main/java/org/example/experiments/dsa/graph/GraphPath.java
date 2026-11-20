package org.example.experiments.dsa.graph;

/**
 * Path ⊆ Trail ⊆ Walk
 * <table>
 *   <thead>
 *     <tr>
 *       <th>Vertex Repetition Allowed?</th>
 *       <th>Edge Repetition Allowed?</th>
 *       <th>Description</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td><strong>No</strong> (all vertices are unique)</td>
 *       <td><strong>No</strong> (implied by no vertex repetition)</td>
 *       <td>A simple path with no loops or repeated nodes. This is what Dijkstra’s algorithm always outputs.</td>
 *     </tr>
 *   </tbody>
 * </table>
 */
public interface GraphPath<V> extends GraphTrail<V> {
    /**
     * A path guarantees all vertices are distinct
     */
    boolean hasRepeatedVertices();
}
