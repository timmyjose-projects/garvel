package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.CoreServiceLoader;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.RunCommandParams;
import com.tzj.garvel.common.spi.core.command.result.RunCommandResult;

public class CLIRunCommand extends CLICommand {
    private final String target;

    public CLIRunCommand(final CLICommandOption opts, final String target) {
        super(opts);
        this.target = target;
    }

    /**
     * Try to run the specified command, as specified in the Garvel.gl
     * configuration file.
     */
    @Override
    public void execute() {
        final RunCommandParams params = new RunCommandParams(target);
        try {
            final RunCommandResult result = (RunCommandResult) CoreServiceLoader.INSTANCE.getCoreService().runCommand(CommandType.RUN, params);

            if (result == null) {
                CLIErrorHandler.errorAndExit("run command failed: internal error");
            }

            checkSuccess(result);

        } catch (CommandException e) {
            CLIErrorHandler.errorAndExit("Unable to create run target \"%s\". Reason = %s", target, e.getErrorString());
        }
    }

    private void checkSuccess(final RunCommandResult result) {

    }
}
