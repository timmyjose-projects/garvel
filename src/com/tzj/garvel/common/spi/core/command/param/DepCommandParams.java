package com.tzj.garvel.common.spi.core.command.param;

import com.tzj.garvel.common.spi.core.command.CommandParams;

public class DepCommandParams extends CommandParams {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final boolean showDependencies;

    public DepCommandParams(final String groupId, final String artifactId, final String version, final boolean showDependencies) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.showDependencies = showDependencies;
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

    public boolean isShowDependencies() {
        return showDependencies;
    }
}
