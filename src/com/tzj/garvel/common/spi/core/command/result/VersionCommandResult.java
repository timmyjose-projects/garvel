package com.tzj.garvel.common.spi.core.command.result;

import com.tzj.garvel.common.spi.core.command.CommandResult;

public class VersionCommandResult extends CommandResult {
    private String versionSemverString;

    public VersionCommandResult(final String versionSemverString) {

        this.versionSemverString = versionSemverString;
    }

    public String getVersionSemverString() {
        return versionSemverString;
    }
}
