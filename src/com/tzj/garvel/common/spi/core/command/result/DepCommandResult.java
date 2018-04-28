package com.tzj.garvel.common.spi.core.command.result;

import com.tzj.garvel.common.spi.core.command.CommandResult;

public class DepCommandResult extends CommandResult {
    private String versions;
    private boolean dependenciesInformationAvailable;
    private String dependencyGraphString;

    public DepCommandResult() {
    }

    public boolean isDependenciesInformationAvailable() {
        return dependenciesInformationAvailable;
    }

    public void setDependenciesInformationAvailable(final boolean dependenciesInformationAvailable) {
        this.dependenciesInformationAvailable = dependenciesInformationAvailable;
    }

    public String getVersions() {
        return versions;
    }

    public void setVersions(final String versions) {
        this.versions = versions;
    }

    public String getDependencyGraphString() {
        return dependencyGraphString;
    }

    public void setDependencyGraphString(final String dependencyGraphString) {
        this.dependencyGraphString = dependencyGraphString;
    }
}
