package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.result.BuildCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.spi.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.job.BuildJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class BuildCommand implements Command {
    @Override
    public CommandResult execute(final CommandParams params) {
        final Job<BuildCommandResult> job = new BuildJob<>();
        final Future<BuildCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().submitJob(job);

        BuildCommandResult cmdRes = null;
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
