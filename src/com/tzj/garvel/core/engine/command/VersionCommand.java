package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.result.VersionCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.CoreServiceImpl;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.engine.job.VersionJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class VersionCommand extends Command {
    public VersionCommand() {
        super(null);
    }

    /**
     * `version` has no prerequisites.
     *
     * @throws CommandException
     */
    @Override
    protected void executePrerequisite() throws CommandException {
        return;
    }

    @Override
    public CommandResult execute(final CommandParams params) throws CommandException {
        final Job<VersionCommandResult> job = new VersionJob();
        final Future<VersionCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        VersionCommandResult cmdRes = null;

        try {
            cmdRes = task.get();
        } catch (InterruptedException e) {
            throw new CommandException("internal exception");
        } catch (ExecutionException e) {
            if (e.getCause() != null) {
                final JobException je = (JobException) e.getCause();
                throw new CommandException(je.getErrorString());
            }
        }

        return cmdRes;
    }
}
