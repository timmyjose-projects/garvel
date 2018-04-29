package com.tzj.garvel.core.dep.graph;

import com.tzj.garvel.core.dep.api.exception.GraphUncheckedException;
import com.tzj.garvel.core.dep.api.graph.Graph;
import com.tzj.garvel.core.dep.api.graph.GraphKind;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

@SuppressWarnings("unchecked")
public class AdjacencySet implements Graph {
    private static final long serialVersionUID = -847254791596283923L;

    private List<Vertex> vertices;
    private GraphKind kind;

    // needed for deserialization
    public AdjacencySet() {
    }

    public AdjacencySet(final int n, final GraphKind kind) {
        this.vertices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            vertices.add(i, new Vertex(i));
        }

        this.kind = kind;
    }

    public AdjacencySet(final GraphKind kind) {
        this.vertices = new ArrayList<>();
        this.kind = kind;
    }

    @Override
    public void addVertex(final int v) {
        vertices.add(new Vertex(v));
    }

    @Override
    public void addEdge(final int v1, final int v2) {
        if (v1 < 0 || v1 >= size() || v2 < 0 || v2 >= size()) {
            throw new GraphUncheckedException(String.format("invalid vertex or vertices: %d, %d\n", v1, v2));
        }

        vertices.get(v1).vs.add(v2);
        if (kind == GraphKind.UNDIRECTED) {
            vertices.get(v2).vs.add(v1);
        }
    }

    @Override
    public List<Integer> getAdjacentVertices(final int v) {
        if (v < 0 || v >= size()) {
            throw new GraphUncheckedException(String.format("invalid vertex %d\n", v));
        }

        List<Integer> vs = new ArrayList<>(vertices.get(v).vs);

        Collections.sort(vs);

        return vs;
    }

    @Override
    public int getIndegree(final int v) {
        if (kind == GraphKind.UNDIRECTED) {
            throw new GraphUncheckedException("indegree is not specified for undirected graphs\n");
        }

        if (v < 0 || v >= size()) {
            throw new GraphUncheckedException(String.format("invalid vertex, %d\n", v));
        }

        int d = 0;
        for (int i = 0; i < size(); i++) {
            if (vertices.get(i).vs.contains(v)) {
                d++;
            }
        }

        return d;
    }

    @Override
    public int size() {
        return vertices.size();
    }

    @Override
    public void display() {
        for (int i = 0; i < size(); i++) {
            System.out.printf("%d: ", i);
            for (int v : vertices.get(i).vs) {
                System.out.printf("%d ", v);
            }
            System.out.println();
        }
    }

    @Override
    public GraphKind kind() {
        return kind;
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeObject(vertices);
        out.writeObject(kind);
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        vertices = (List<Vertex>) in.readObject();
        kind = (GraphKind) in.readObject();
    }

    static class Vertex implements Externalizable {
        private static final long serialVersionUID = 2962963098964258155L;

        int v;
        Set<Integer> vs;

        // for deserialization
        public Vertex() {
        }

        public Vertex(final int v) {
            this.v = v;
            this.vs = new HashSet<>();
        }

        @Override
        public void writeExternal(final ObjectOutput out) throws IOException {
            out.writeInt(v);
            out.writeObject(vs);
        }

        @Override
        public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
            v = in.readInt();
            vs = (Set<Integer>) in.readObject();
        }
    }
}
