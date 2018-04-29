package com.tzj.garvel.core.dep.graph;

import com.tzj.garvel.core.dep.api.exception.GraphCheckedException;
import com.tzj.garvel.core.dep.api.exception.GraphUncheckedException;
import com.tzj.garvel.core.dep.api.graph.Graph;
import com.tzj.garvel.core.dep.api.graph.GraphCallback;
import com.tzj.garvel.core.dep.api.graph.GraphKind;

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
    public static void topologicalSort(Graph g, GraphCallback<List<Integer>> cb) throws GraphCheckedException {
        if (g.kind() != GraphKind.DIRECTED) {
            throw new GraphUncheckedException("Topological Sort is only supported for Directed Graphs\n");
        }

        cb.pre();

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

        cb.invoke(vs);
        cb.post();
    }

    /**
     * Depth-First Traversal.
     *
     * @param g
     * @param cb
     * @throws GraphCheckedException
     */
    public static void dfs(Graph g, int s, GraphCallback<Integer> cb) {
        boolean[] visited = new boolean[g.size()];

        dfsInternal(g, visited, s, cb);
    }

    private static void dfsInternal(final Graph g, final boolean[] visited, final int s, final GraphCallback<Integer> cb) {
        if (visited[s]) {
            return;
        }

        cb.pre();
        visited[s] = true;
        cb.invoke(s);

        for (int v : g.getAdjacentVertices(s)) {
            dfsInternal(g, visited, v, cb);
        }

        cb.post();
    }

    /**
     * Breadth-First Traversal.
     *
     * @param g
     * @param s
     * @param cb
     */
    public static void bfs(Graph g, int s, GraphCallback<Integer> cb) {
        boolean[] visited = new boolean[g.size()];

        Queue<Integer> q = new ArrayDeque<>();
        q.add(s);

        while (!q.isEmpty()) {
            int v = q.remove();

            cb.pre();
            if (visited[v]) {
                continue;
            }

            visited[v] = true;
            cb.invoke(v);

            for (int vv : g.getAdjacentVertices(v)) {
                if (!visited[vv]) {
                    q.add(vv);
                }
            }
        }

        cb.post();
    }
}
