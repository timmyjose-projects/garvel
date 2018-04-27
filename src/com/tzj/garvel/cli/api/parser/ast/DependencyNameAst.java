package com.tzj.garvel.cli.api.parser.ast;

public class DependencyNameAst {
    private String groupId;
    private String artifactId;

    public DependencyNameAst(final String groupId, final String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }
}
