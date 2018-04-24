package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.param.InstallCommandParams;
import com.tzj.garvel.core.engine.Command;

public class DepCommand extends Command {
    public DepCommand() {
        super(new InstallCommand());
    }

    /**
     * `dep` depends on `install`. Later on, if needed,
     * a dependency on `update` can also be added here.
     *
     * @throws CommandException
     */
    @Override
    protected void executePrerequisite() throws CommandException {
        try {
            prerequisiteCommand.run(new InstallCommandParams());
        } catch (CommandException e) {
            throw new CommandException(String.format("Prerequisite (install) for dep command failed, %s\n", e.getErrorString()));
        }
    }

    @Override
    public CommandResult execute(final CommandParams params) throws CommandException {
        return null;
    }
}
