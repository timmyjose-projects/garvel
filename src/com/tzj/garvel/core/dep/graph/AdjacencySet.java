package com.tzj.garvel.core.dep.graph;

import com.tzj.garvel.core.dep.api.exception.GraphUncheckedException;
import com.tzj.garvel.core.dep.api.graph.Graph;

import java.util.*;

public class AdjacencySet implements Graph {
    private Vertex[] vertices;
    private int n;
    private Graph.Kind kind;

    public AdjacencySet(final int n, final Graph.Kind kind) {
        this.n = n;

        this.vertices = new Vertex[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = new Vertex(i);
        }

        this.kind = kind;
    }

    @Override
    public void addEdge(final int v1, final int v2) {
        if (v1 < 0 || v1 >= n || v2 < 0 || v2 >= n) {
            throw new GraphUncheckedException(String.format("invalid vertex of vertices %d, %d\n", v1, v2));
        }

        vertices[v1].vs.add(v2);
        if (kind == Kind.UNDIRECTED) {
            vertices[v2].vs.add(v1);
        }
    }

    @Override
    public List<Integer> getAdjacentVertices(final int v) {
        if (v < 0 || v >= n) {
            throw new GraphUncheckedException(String.format("invalid vertex %d\n", v));
        }

        List<Integer> vs = new ArrayList<>(vertices[v].vs);

        Collections.sort(vs);

        return vs;
    }

    @Override
    public int getIndegree(final int v) {
        if (kind == Kind.UNDIRECTED) {
            throw new GraphUncheckedException("indegree is not specified for undirected graphs\n");
        }

        if (v < 0 || v >= n) {
            throw new GraphUncheckedException(String.format("invalid vertex, %d\n", v));
        }

        int d = 0;
        for (int i = 0; i < n; i++) {
            if (vertices[i].vs.contains(v)) {
                d++;
            }
        }

        return d;
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public void display() {
        for (int i = 0; i < n; i++) {
            System.out.printf("%d: ", i);
            for (int v : vertices[i].vs) {
                System.out.printf("%d ", v);
            }
            System.out.println();
        }
    }

    @Override
    public Kind kind() {
        return kind;
    }

    private class Vertex {
        int v;
        Set<Integer> vs;

        public Vertex(final int v) {
            this.v = v;
            this.vs = new HashSet<>();
        }
    }
}
