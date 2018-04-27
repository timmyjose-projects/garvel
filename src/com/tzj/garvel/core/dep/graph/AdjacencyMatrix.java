package com.tzj.garvel.core.dep.graph;

import com.tzj.garvel.common.spi.error.GarvelUncheckedException;
import com.tzj.garvel.core.dep.api.exception.GraphUncheckedException;
import com.tzj.garvel.core.dep.api.graph.Graph;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdjacencyMatrix implements Graph {
    private static final long serialVersionUID = 1410137352373349136L;
    private int[][] a;
    private int n;
    private Graph.Kind kind;

    public AdjacencyMatrix(final int n, final Graph.Kind kind) {
        this.n = n;
        this.a = new int[n][n];
        this.kind = kind;
    }

    @Override
    public void addEdge(final int v1, final int v2) {
        if (v1 < 0 || v1 >= n || v2 < 0 || v2 >= n) {
            throw new GraphUncheckedException(String.format("invalid vertex or vertices %d, %d\n", v1, v2));
        }

        a[v1][v2] = 1;
        if (kind == Kind.UNDIRECTED) {
            a[v2][v1] = 1;
        }
    }

    @Override
    public List<Integer> getAdjacentVertices(final int v) {
        if (v < 0 || v >= n) {
            throw new GraphUncheckedException(String.format("invalid vertex %d\n", v));
        }

        List<Integer> vs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (a[v][i] == 1) {
                vs.add(i);
            }
        }

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
            if (a[i][v] == 1) {
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
            for (int j = 0; j < n; j++) {
                System.out.printf("%d ", a[i][j]);
            }
            System.out.println();
        }
    }

    @Override
    public Kind kind() {
        return kind;
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {

    }
}
