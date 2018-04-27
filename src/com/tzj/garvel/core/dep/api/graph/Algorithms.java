package com.tzj.garvel.core.dep.api.graph;

import com.tzj.garvel.core.dep.api.exception.GraphCheckedException;

import java.util.*;

public class Algorithms {
    private Algorithms() {
    }

    public static List<Integer> topologicalSort(Graph g) throws GraphCheckedException {
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
