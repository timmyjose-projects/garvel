package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.param.CleanCommandParams;
import com.tzj.garvel.common.spi.core.command.param.InstallCommandParams;
import com.tzj.garvel.common.spi.core.command.result.CleanCommandResult;
import com.tzj.garvel.common.spi.core.command.result.InstallCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.engine.job.BuildJob;
import com.tzj.garvel.core.engine.job.CleanJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CleanCommand extends Command {
    public CleanCommand() {
        super(new InstallCommand());
    }

    /**
     * Clean has a depency on install.
     *
     * @throws CommandException
     */
    @Override
    protected void executePrerequisite() throws CommandException {
        try {
            prerequisiteCommand.run(new InstallCommandParams());
        } catch (CommandException e) {
            throw new CommandException(String.format("Prerequisite (install) for clean command failed, %s\n", e.getErrorString()));
        }
    }

    @Override
    public CommandResult execute(final CommandParams params) throws CommandException {
        final CleanCommandParams cmdParams = (CleanCommandParams)params;
        final Job<CleanCommandResult> job = new CleanJob(cmdParams);
        final Future<CleanCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        CleanCommandResult cmdRes = null;
        try {
            cmdRes = task.get();
        } catch (InterruptedException e) {
            throw new CommandException("internal exception");
        } catch (ExecutionException e) {
            if (e.getCause() != null) {
                final JobException je = (JobException) e.getCause();
                throw new CommandException(je.getErrorSting());
            }
        }

        return cmdRes;
    }
}
