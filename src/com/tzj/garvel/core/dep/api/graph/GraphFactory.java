package com.tzj.garvel.core.dep.api.graph;

import com.tzj.garvel.core.dep.graph.AdjacencySet;

public class GraphFactory {
    private GraphFactory() {
    }

    public static Graph getGraphImpl(final GraphImplType type, final GraphKind kind) {
        Graph g = null;

        switch (type) {
            case ADJACENCY_SET:
                g = new AdjacencySet(kind);
                break;
        }

        return g;
    }
}
