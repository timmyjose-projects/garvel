package com.tzj.garvel.common.spi.core.command.result;

import com.tzj.garvel.common.spi.core.command.CommandResult;

import java.nio.file.Path;

public class CleanCommandResult extends CommandResult {
    private boolean targetDirDeleted;

    public CleanCommandResult(final boolean targetDirDeleted) {

        this.targetDirDeleted = targetDirDeleted;
    }

    public boolean isTargetDirDeleted() {
        return targetDirDeleted;
    }
}
