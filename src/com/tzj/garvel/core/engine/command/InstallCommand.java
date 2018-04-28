package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.param.InstallCommandParams;
import com.tzj.garvel.common.spi.core.command.result.InstallCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.engine.job.InstallJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Install the Garvel registry and cache at $HOME/.garvel or %USERPROFILE%/.garvel.
 */
public class InstallCommand extends Command {
    /**
     * `install` command has no prerequisites
     */
    public InstallCommand() {
        super(null);
    }

    @Override
    protected void executePrerequisite() throws CommandException {
        return;
    }

    @Override
    public CommandResult execute(final CommandParams params) throws CommandException {
        final InstallCommandParams cmdParams = (InstallCommandParams) params;
        final Job<InstallCommandResult> job = new InstallJob(cmdParams);
        final Future<InstallCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        InstallCommandResult cmdRes = null;
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
