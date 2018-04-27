package com.tzj.garvel.playground.javacompiler;

import com.tzj.garvel.core.dep.api.exception.GraphCheckedException;
import com.tzj.garvel.core.dep.graph.AdjacencySet;
import com.tzj.garvel.core.dep.graph.Algorithms;
import com.tzj.garvel.core.dep.api.graph.Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyGraphDisplayer {
    private static Map<Integer, DepArtifact> mapping = new HashMap<>();

    public static void main(String[] args) throws GraphCheckedException {
        mapping.put(0, new DepArtifact("org.foo", "foo", "1.0"));
        mapping.put(1, new DepArtifact("org.bar", "bar", "2.0"));
        mapping.put(2, new DepArtifact("org.baz", "baz", "3.0"));
        mapping.put(3, new DepArtifact("com.fubar", "fubar", "4.0"));
        mapping.put(4, new DepArtifact("com.quux", "quux", "5.0"));
        mapping.put(5, new DepArtifact("net.arghh", "arghh", "6.0"));

        Graph g = new AdjacencySet(6, Graph.Kind.DIRECTED);
        g.addEdge(0, 1);
        g.addEdge(0, 4);
        g.addEdge(1, 3);
        g.addEdge(3, 2);
        g.addEdge(4, 2);
        g.addEdge(1, 5);

        g.display();

        boolean[] visited = new boolean[g.size()];

        Callback<Integer> cb = new DisplayCallback();
        dfs(g, visited, 0, cb);

        System.out.println(((DisplayCallback) cb).getSb().toString());
    }

    private static void dfs(Graph g, boolean[] visited, int s, Callback<Integer> cb) {
        if (visited[s]) {
            return;
        }

        cb.invoke(s);
        visited[s] = true;

        for (int v : g.getAdjacentVertices(s)) {
            dfs(g, visited, v, cb);
        }
        cb.post();
    }

    static interface Callback<T> {
        void invoke(T t);

        void post();
    }

    static class DisplayCallback implements Callback<Integer> {
        private StringBuffer sb;
        private String indent;

        public DisplayCallback() {
            this.sb = new StringBuffer();
            this.indent = "";
        }

        public StringBuffer getSb() {
            return sb;
        }

        @Override
        public void invoke(final Integer v) {
            sb.append(String.format("%s+ ", indent));
            sb.append(mapping.get(v));
            sb.append("\n");
            indent += "| ";
        }

        @Override
        public void post() {
            indent = indent.substring(0, indent.length() - 2);
        }
    }

    static class DepArtifact {
        private String groupId;
        private String artifactId;
        private String version;

        public DepArtifact(final String groupId, final String artifactId, final String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }

        @Override
        public String toString() {
            return "{ " + groupId + ", " + artifactId + ", " + version + " }";
        }

        public String getGroupId() {
            return groupId;
        }

        public String getArtifactId() {
            return artifactId;
        }

        public String getVersion() {
            return version;
        }
    }
}
