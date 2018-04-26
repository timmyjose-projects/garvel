package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.CoreServiceLoader;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.InstallCommandParams;
import com.tzj.garvel.common.spi.core.command.result.InstallCommandResult;
import com.tzj.garvel.common.util.UtilServiceImpl;

public class CLIInstallCommand extends CLICommand {
    public CLIInstallCommand(final CLICommandOption opts) {
        super(opts);
    }

    @Override
    public void execute() {
        final InstallCommandParams params = new InstallCommandParams();
        try {
            final InstallCommandResult result = (InstallCommandResult) CoreServiceLoader.INSTANCE.getCoreService().runCommand(CommandType.INSTALL, params);

            checkSuccess(result);
            UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, "Installed Garvel successfully.");
        } catch (CommandException e) {
            CLIErrorHandler.errorAndExit("Unable to install Garvel. Reason = %s", e.getErrorString());
        }
    }

    private void checkSuccess(final InstallCommandResult result) {
        if (result.getGarvelRoot() && result.getGarvelCache()) {
            return;
        }

        CLIErrorHandler.errorAndExit("Unable to install Garvel.Reason = failed to create one of the directories\n");
    }
}
