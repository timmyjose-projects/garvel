package com.tzj.garvel.core.dep.api;

/**
 * Represents the basic artifact in Garvel.
 */
public class Artifact {
    private String groupId;
    private String artifactId;
    private String version;
    private int id;

    public Artifact(final int id, final String groupId, final String artifactId, final String version) {
        this.id = id;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(final String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }
}
