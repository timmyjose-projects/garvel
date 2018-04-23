package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.core.engine.Command;

/**
 * Install the Garvel registry and cache at $HOME/.garvel or %USERPROFILE%/.garvel.
 */
public class InstallCommand extends Command {
    // `install` command has no prerequisites
    public InstallCommand() {
        super(null);
    }

    @Override
    protected void executePrerequisite() throws CommandException {
        return;
    }

    @Override
    public CommandResult execute(final CommandParams params) throws CommandException {
        return null;
    }
}
