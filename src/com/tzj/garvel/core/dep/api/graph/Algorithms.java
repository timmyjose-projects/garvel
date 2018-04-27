package com.tzj.garvel.core.dep.api.graph;

import com.tzj.garvel.core.dep.api.exception.GraphCheckedException;
import com.tzj.garvel.core.dep.api.exception.GraphUncheckedException;

import java.util.*;

public class Algorithms {
    private Algorithms() {
    }

    /**
     * Topological Sort.
     * <p>
     * O(V+E)
     *
     * @param g
     * @return
     * @throws GraphCheckedException
     */
    public static List<Integer> topologicalSort(Graph g) throws GraphCheckedException {
        if (g.kind() != Graph.Kind.DIRECTED) {
            throw new GraphUncheckedException("Topological Sort is only supported for Directed Graphs\n");
        }

        Queue<Integer> q = new ArrayDeque<>();

        Map<Integer, Integer> indegs = new HashMap<>();
        for (int i = 0; i < g.size(); i++) {
            int d = g.getIndegree(i);

            if (d == 0) {
                q.add(d);
            }

            indegs.put(i, d);
        }

        List<Integer> vs = new ArrayList<>();
        while (!q.isEmpty()) {
            int v = q.remove();

            vs.add(v);
            for (int vv : g.getAdjacentVertices(v)) {
                int d = indegs.get(vv) - 1;

                indegs.put(vv, d);
                if (d == 0) {
                    q.add(vv);
                }
            }
        }

        if (vs.size() != g.size()) {
            throw new GraphCheckedException("Cycle detected");
        }

        return vs;
    }
}
