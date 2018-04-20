package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.result.RunCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.job.RunJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RunCommand implements Command {
    @Override
    public CommandResult execute(final CommandParams params) {
        final Job<RunCommandResult> job = new RunJob();
        final Future<RunCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        RunCommandResult cmdRes = null;

        try {
            cmdRes = task.get();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return cmdRes;
    }
}
