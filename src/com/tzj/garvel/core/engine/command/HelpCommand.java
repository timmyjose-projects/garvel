package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.param.HelpCommandParams;
import com.tzj.garvel.common.spi.core.command.result.HelpCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.job.HelpJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Retrieve the help pages for the given command.
 */
public class HelpCommand implements Command {
    @Override
    public CommandResult execute(final CommandParams params) {
        final HelpCommandParams cmdParams = (HelpCommandParams) params;
        final Job<HelpCommandResult> job = new HelpJob(cmdParams.getCommandName());
        final Future<HelpCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        HelpCommandResult cmdRes = null;
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
