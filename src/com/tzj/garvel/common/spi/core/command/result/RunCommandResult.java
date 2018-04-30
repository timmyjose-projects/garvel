package com.tzj.garvel.common.spi.core.command.result;

import com.tzj.garvel.common.spi.core.command.CommandResult;

public class RunCommandResult extends CommandResult {
    private boolean runSuccessful;

    public RunCommandResult() {
    }

    public boolean isRunSuccessful() {
        return runSuccessful;
    }

    public void setRunSuccessful(final boolean runSuccessful) {
        this.runSuccessful = runSuccessful;
    }
}
