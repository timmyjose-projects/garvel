package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.result.ListCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.job.ListJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ListCommand implements Command {
    @Override
    public CommandResult execute(final CommandParams params) {
        final Job<ListCommandResult> job = new ListJob();
        final Future<ListCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        ListCommandResult cmdRes = null;

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
