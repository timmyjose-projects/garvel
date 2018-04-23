package com.tzj.garvel.core.engine;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;

/**
 * Uses the Template Pattern to simulate life-cycle dependencies. For this reason
 * each dependenct command must have a surefire way of determining if it actually needs
 * to run. This is especially true in the case of the `install` command, on which almost
 * all other commands have a dependency.
 */
public abstract class Command {
    protected Command prerequisiteCommand;

    public Command(final Command prerequisiteCommand) {
        this.prerequisiteCommand = prerequisiteCommand;
    }

    /**
     * The template:
     * 1. execute the prerequisite command (which chains up).
     * 2. execute the current command.
     *
     * @param cmdParams
     * @return
     * @throws CommandException
     */
    public CommandResult run(final CommandParams cmdParams) throws CommandException {
        executePrerequisite();
        return execute(cmdParams);
    }

    protected abstract void executePrerequisite() throws CommandException;

    protected abstract CommandResult execute(CommandParams params) throws CommandException;
}
