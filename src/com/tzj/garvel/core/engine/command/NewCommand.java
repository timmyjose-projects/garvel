package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.result.NewCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.job.NewJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class NewCommand implements Command {
    @Override
    public CommandResult execute(final CommandParams params) {
        final Job<CommandResult> job = new NewJob<>();
        final Future<NewCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().submitJob(job);

        NewCommandResult cmdRes = null;
        try {
            // add a timeout value?
            cmdRes = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return cmdRes;
    }
}