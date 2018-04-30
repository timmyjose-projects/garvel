package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.UninstallCommandParams;
import com.tzj.garvel.common.spi.core.command.result.UninstallCommandResult;
import com.tzj.garvel.common.util.UtilServiceImpl;
import com.tzj.garvel.core.CoreServiceImpl;

public class CLIUninstallCommand extends CLICommand {
    public CLIUninstallCommand(final CLICommandOption opts) {
        super(opts);
    }

    @Override
    public void execute() {
        final UninstallCommandParams params = new UninstallCommandParams();
        try {
            final UninstallCommandResult result = (UninstallCommandResult) CoreServiceImpl.INSTANCE.runCommand(CommandType.UNINSTALL, params);

            if (result == null) {
                CLIErrorHandler.errorAndExit("uninstall command failed: internal error");
            }

            UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, "Uninstalled Garvel successfully.");
        } catch (CommandException e) {
            CLIErrorHandler.errorAndExit("Unable to uninstall Garvel.Reason = %s. Please remove the $HOME/.garvel directory manually.\n", e.getErrorString());
        }
    }
}
