package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.param.InstallCommandParams;
import com.tzj.garvel.common.spi.core.command.param.NewCommandParams;
import com.tzj.garvel.common.spi.core.command.result.NewCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.engine.job.NewJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Crate a new Garvel project.
 */
public class NewCommand extends Command {
    public NewCommand() {
        super(new InstallCommand());
    }

    /**
     * `new` command depends on `install` command.
     *
     * @throws CommandException
     */
    @Override
    protected void executePrerequisite() throws CommandException {
        try {
            prerequisiteCommand.run(new InstallCommandParams());
        } catch (CommandException e) {
            throw new CommandException(String.format("Prerequisite (install) for new command failed, %s\n", e.getErrorString()));
        }
    }

    @Override
    public CommandResult execute(final CommandParams params) throws CommandException {
        final NewCommandParams cmdParams = (NewCommandParams) params;
        final Job<NewCommandResult> job = new NewJob(cmdParams.getVcs(), cmdParams.getPath());
        final Future<NewCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        NewCommandResult cmdRes = null;
        try {
            cmdRes = task.get();
        } catch (InterruptedException e) {
            throw new CommandException("internal exception");
        } catch (ExecutionException e) {
            if (e.getCause() != null && e.getCause() instanceof JobException) {
                final JobException je = (JobException) e.getCause();
                throw new CommandException(je.getErrorString());
            }
        }

        return cmdRes;
    }
}