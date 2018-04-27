package com.tzj.garvel.common.spi.core.command.param;

import com.tzj.garvel.common.spi.core.command.CommandParams;

public class DepCommandParams extends CommandParams {
    private final String dependencyName;
    private final boolean showDependencies;

    public DepCommandParams(final String dependencyName, final boolean showDependencies) {
        this.dependencyName = dependencyName;
        this.showDependencies = showDependencies;
    }

    public String getDependencyName() {
        return dependencyName;
    }

    public boolean isShowDependencies() {
        return showDependencies;
    }
}
