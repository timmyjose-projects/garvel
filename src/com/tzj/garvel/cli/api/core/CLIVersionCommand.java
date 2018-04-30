package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.CoreServiceLoader;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.VersionCommandParams;
import com.tzj.garvel.common.spi.core.command.result.VersionCommandResult;
import com.tzj.garvel.common.spi.util.UtilService;
import com.tzj.garvel.common.util.UtilServiceImpl;

/**
 * Get the current Garvel version from core.
 */
public class CLIVersionCommand extends CLICommand {
    public CLIVersionCommand(final CLICommandOption opts) {
        super(opts);
    }

    @Override
    public void execute() {
        VersionCommandParams params = new VersionCommandParams();
        VersionCommandResult result = null;
        try {
            result = (VersionCommandResult) CoreServiceLoader.INSTANCE.getCoreService().runCommand(CommandType.VERSION, params);

            if (result == null) {
                CLIErrorHandler.errorAndExit("version command failed: internal error");
            }

            UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, result.getVersionSemverString());
        } catch (CommandException e) {
            CLIErrorHandler.errorAndExit("Unable to fetch version. Reason = " + e.getLocalizedMessage());
        }
    }
}
