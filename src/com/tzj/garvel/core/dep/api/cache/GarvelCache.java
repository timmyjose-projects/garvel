package com.tzj.garvel.core.dep.api.cache;

import com.tzj.garvel.core.dep.api.Artifact;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;

/**
 * Represents the mapping between the artifacts and the downloaded
 * resources. Uses the Maven coordinates as the key.
 */
@SuppressWarnings("unchecked")
public class GarvelCache implements Externalizable {
    private static final long serialVersionUID = -7195654373684604629L;

    private Map<Artifact, String> cacheMapping;

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeObject(cacheMapping);
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        cacheMapping = (Map<Artifact, String>) in.readObject();
    }
}
