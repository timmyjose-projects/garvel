package com.tzj.garvel.core.dep;

import com.tzj.garvel.core.dep.api.Artifact;
import com.tzj.garvel.core.dep.api.graph.Graph;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper class that holds the actual Dependency Graph as well as stores the
 * current mapping between the integer ids and the Maven coordinates of the
 * arrifact.
 */
@SuppressWarnings("unchecked")
public class DependencyGraph implements Externalizable {
    private static final long serialVersionUID = 4277358070820437781L;

    private Graph g;
    private Map<Integer, Artifact> artifactMapping;

    public DependencyGraph(final Graph g) {
        this.g = g;
        this.artifactMapping = new HashMap<>();
    }

    public Graph getG() {
        return g;
    }

    public Map<Integer, Artifact> getArtifactMapping() {

        return artifactMapping;
    }

    public void addArtifactMapping(final Integer id, final Artifact artifact) {
        artifactMapping.put(id, artifact);
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeObject(g);
        out.writeObject(artifactMapping);
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.g = (Graph) in.readObject();
        this.artifactMapping = (Map<Integer, Artifact>) in.readObject();
    }
}
