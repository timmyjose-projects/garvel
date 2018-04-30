package com.tzj.garvel.core.dep.graph;

import com.tzj.garvel.core.dep.api.graph.GraphCallback;
import com.tzj.garvel.core.dep.api.Artifact;

import java.util.Map;

/**
 * Used to display the transitive dependencies of an jar using DFS.
 */
public class GraphDisplayCallback implements GraphCallback<Integer> {
    private Map<Integer, Artifact> mapping;
    private StringBuffer buffer;
    private String indent;

    public GraphDisplayCallback(final Map<Integer, Artifact> mapping) {
        this.mapping = mapping;
    }

    @Override
    public void pre() {
        this.buffer = new StringBuffer();
        this.indent = "";
    }

    @Override
    public void invoke(final Integer v) {
        buffer.append(String.format("%s+ ", indent));
        buffer.append(mapping.get(v));
        buffer.append("\n");
        indent += "| ";
    }

    @Override
    public void post() {
        indent = indent.substring(0, indent.length() - 2);
    }
}
