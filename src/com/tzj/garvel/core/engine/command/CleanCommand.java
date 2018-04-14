package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.result.CleanCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.job.BuildJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CleanCommand implements Command {
    @Override
    public CommandResult execute(final CommandParams params) {
        final Job<CleanCommandResult> job = new BuildJob<>();
        final Future<CleanCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().submitJob(job);

        CleanCommandResult cmdRes = null;
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
