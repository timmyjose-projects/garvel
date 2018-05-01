package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.CoreServiceLoader;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.RunCommandParams;
import com.tzj.garvel.common.spi.core.command.result.RunCommandResult;
import com.tzj.garvel.common.util.UtilServiceImpl;

public class CLIRunCommand extends CLICommand {
    private final String target;
    private String[] args;

    public CLIRunCommand(final CLICommandOption opts, final String target, final String[] args) {
        super(opts);
        this.target = target;
        this.args = args;
    }

    /**
     * Try to run the specified command, as specified in the Garvel.gl
     * configuration file.
     */
    @Override
    public void execute() {
        final RunCommandParams params = new RunCommandParams(target, args);
        try {
            final RunCommandResult result = (RunCommandResult) CoreServiceLoader.INSTANCE.getCoreService().runCommand(CommandType.RUN, params);

            if (result == null) {
                CLIErrorHandler.errorAndExit("run command failed: internal error");
            }

            checkSuccess(result);

            UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, "Target \"%s\" was run successfully\n", target);
        } catch (CommandException e) {
            if (target.equalsIgnoreCase("none")) {
                CLIErrorHandler.errorAndExit("Unable to run project. Reason = %s", e.getErrorString());
            } else {
                CLIErrorHandler.errorAndExit("Unable to run target \"%s\". Reason = %s", target, e.getErrorString());
            }
        }
    }

    private void checkSuccess(final RunCommandResult result) {
        if (result.isRunSuccessful()) {
            return;
        }

        CLIErrorHandler.errorAndExit("Unable to run target \"%s\".\n", target);
    }
}
