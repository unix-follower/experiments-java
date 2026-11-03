package org.example.experiments.dsa.graph;

/**
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
 *       <td>Yes (can revisit vertices)</td>
 *       <td><strong>No</strong> (all edges must be unique)</td>
 *       <td>A walk that never crosses the same physical edge twice (e.g., Eulerian paths).</td>
 *     </tr>
 *   </tbody>
 * </table>
 */
public interface GraphTrail<V> extends GraphWalk<V> {
    /**
     * A trail guarantees all edges are distinct
     */
    boolean hasRepeatedEdges();
}
