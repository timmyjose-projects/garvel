package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.param.DepCommandParams;
import com.tzj.garvel.common.spi.core.command.param.InstallCommandParams;
import com.tzj.garvel.common.spi.core.command.result.DepCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.engine.job.DepJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
        final DepCommandParams cmdParams = (DepCommandParams) params;
        final Job<DepCommandResult> job = new DepJob(cmdParams.getDependencyName(), cmdParams.isShowDependencies());
        final Future<DepCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        DepCommandResult cmdRes = null;
        try {
            cmdRes = task.get();
        } catch (InterruptedException e) {
            throw new CommandException("internal error");
        } catch (ExecutionException e) {
            if (e.getCause() != null && e.getCause() instanceof JobException) {
                final JobException je = (JobException) e.getCause();
                throw new CommandException(je.getErrorSting());
            }
        }

        return cmdRes;
    }
}
