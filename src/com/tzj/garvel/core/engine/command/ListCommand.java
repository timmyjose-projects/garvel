package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.param.ListCommandParams;
import com.tzj.garvel.common.spi.core.command.result.ListCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.engine.job.ListJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ListCommand extends Command {
    public ListCommand() {
        super(null);
    }

    /**
     * `list` command has no prerequisites.
     *
     * @throws CommandException
     */
    @Override
    protected void executePrerequisite() throws CommandException {
        return;
    }

    @Override
    public CommandResult execute(final CommandParams params) throws CommandException {
        final ListCommandParams cmdParams = (ListCommandParams)params;
        final Job<ListCommandResult> job = new ListJob(cmdParams);
        final Future<ListCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        ListCommandResult cmdRes = null;

        try {
            cmdRes = task.get();
        } catch (InterruptedException e) {
            throw new CommandException("internal error");
        } catch (ExecutionException e) {
            if (e.getCause() != null) {
                final JobException je = (JobException) e.getCause();
                throw new CommandException(je.getErrorSting());
            }
        }

        return cmdRes;
    }
}
