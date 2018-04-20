package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.result.TestCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.job.TestJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestCommand implements Command {
    @Override
    public CommandResult execute(final CommandParams params) {
        final Job<TestCommandResult> job = new TestJob();
        final Future<TestCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        TestCommandResult cmdRes = null;

        try {
            cmdRes = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return cmdRes;
    }
}
