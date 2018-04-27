package com.tzj.garvel.core.dep.api.graph;

import java.util.List;

public interface Graph {
    void addEdge(int v1, int v2);

    List<Integer> getAdjacentVertices(int v);

    int getIndegree(int v);

    int size();

    void display();

    enum Kind {
        DIRECTED,
        UNDIRECTED;
    }
}
