package com.tzj.garvel.common.spi.core.command.param;

import com.tzj.garvel.common.spi.core.command.CommandParams;

public class DepCommandParams extends CommandParams {
    private final String groupId;
    private final String artifactId;
    private final boolean showDependencies;

    public DepCommandParams(final String groupId, final String artifactId, final boolean showDependencies) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.showDependencies = showDependencies;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public boolean isShowDependencies() {
        return showDependencies;
    }
}
