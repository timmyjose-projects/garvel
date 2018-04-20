package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;

/**
 * This will simply dispatch the execution result to the Core
 * module.
 */
public abstract class CLICommand {
    protected CLICommandOption opts;

    protected CLICommand(CLICommandOption opts) {
        this.opts = opts;
    }

    public CLICommandOption getOpts() {
        return opts;
    }

    public abstract void execute();
}
