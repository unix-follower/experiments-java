package org.example.experiments.dsa.graph;

import java.util.List;

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
 *       <td>Yes (can loop)</td>
 *       <td>Yes (can traverse same edge twice)</td>
 *       <td>A general sequence of alternating vertices and edges.</td>
 *     </tr>
 *   </tbody>
 * </table>
 */
public interface GraphWalk<V> {
    List<V> getVertices();

    double getTotalWeight();

    boolean containsVertex(V vertex);
}
