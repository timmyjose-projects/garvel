package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.param.BuildCommandParams;
import com.tzj.garvel.common.spi.core.command.result.TestCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.engine.job.TestJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestCommand extends Command {
    public TestCommand() {
        super(new BuildCommand());
    }

    /**
     * `test` depends on `build`.
     *
     * @throws CommandException
     */
    @Override
    protected void executePrerequisite() throws CommandException {
        try {
            prerequisiteCommand.run(new BuildCommandParams());
        } catch (CommandException e) {
            throw new CommandException(String.format("Prerequisite (build) for test command failed, %s\n", e.getErrorString()));
        }
    }

    @Override
    public CommandResult execute(final CommandParams params) throws CommandException {
        final Job<TestCommandResult> job = new TestJob();
        final Future<TestCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        TestCommandResult cmdRes = null;

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
