package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.result.VersionCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.CoreServiceImpl;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.job.VersionJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class VersionCommand implements Command {
    @Override
    public CommandResult execute(final CommandParams params) throws CommandException {
        final Job<VersionCommandResult> job = new VersionJob();
        final Future<VersionCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        VersionCommandResult cmdRes = null;

        try {
            cmdRes = task.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new CommandException("Internal error while executing command");
        }

        return cmdRes;
    }
}
