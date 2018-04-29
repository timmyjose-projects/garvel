package com.tzj.garvel.core.dep.api.cache;

import com.tzj.garvel.core.dep.api.Artifact;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the mapping between the artifacts and the downloaded
 * resources. Uses the Maven coordinates as the key.
 */

@SuppressWarnings("unchecked")
public class GarvelCache implements Externalizable {
    private static final long serialVersionUID = -7195654373684604629L;

    private Map<Artifact, String> cacheMapping;

    public GarvelCache() {
        cacheMapping = new HashMap<>();
    }

    public void addArtifact(final Artifact artifact, final String path) {
        cacheMapping.put(artifact, path);
    }

    public boolean artifactAvailable(final Artifact artifact) {
        return cacheMapping.containsKey(artifact);
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeObject(cacheMapping);
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        cacheMapping = (Map<Artifact, String>) in.readObject();
    }

    public List<String> getPaths(final List<Artifact> artifacts) {
        List<String> paths = new ArrayList<>();

        for (final Artifact artifact : artifacts) {
            paths.add(cacheMapping.get(artifact));
        }

        return paths;
    }
}
