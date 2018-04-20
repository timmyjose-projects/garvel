package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.CoreServiceLoader;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.VersionCommandParams;
import com.tzj.garvel.common.spi.core.command.result.VersionCommandResult;

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
        VersionCommandResult version = null;
        try {
            version = (VersionCommandResult) CoreServiceLoader.INSTANCE.getCoreService().runCommand(CommandType.VERSION, params);
        } catch (CommandException e) {
            CLIErrorHandler.errorAndExit("Unable to fetch version. Reason = " + e.getLocalizedMessage());
        }

        System.out.println(version.getVersionSemverString());
    }
}
