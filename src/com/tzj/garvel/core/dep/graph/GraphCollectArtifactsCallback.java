package com.tzj.garvel.core.dep.graph;

import com.tzj.garvel.core.dep.DependencyGraph;
import com.tzj.garvel.core.dep.api.Artifact;
import com.tzj.garvel.core.dep.api.graph.GraphCallback;

import java.util.List;
import java.util.Map;

/**
 * This callback is used by Topological sort to return the actual artifacts
 * associated with the integer ids, so that a separate pass over the same data
 * is not required.
 */
public class GraphCollectArtifactsCallback implements GraphCallback<List<Integer>> {
    private final List<Artifact> artifactsOrdering;
    private Map<Integer, Artifact> mapping;

    public GraphCollectArtifactsCallback(final Map<Integer, Artifact> mapping, final List<Artifact> artifactsOrdering) {
        this.mapping = mapping;
        this.artifactsOrdering = artifactsOrdering;
    }

    @Override
    public void pre() {
    }

    @Override
    public void invoke(final List<Integer> ids) {
        for (int id : ids) {
            artifactsOrdering.add(mapping.get(id));
        }
    }

    @Override
    public void post() {
    }
}
