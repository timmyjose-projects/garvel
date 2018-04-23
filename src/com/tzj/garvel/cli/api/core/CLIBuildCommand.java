package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.CoreServiceLoader;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.BuildCommandParams;
import com.tzj.garvel.common.spi.core.command.result.BuildCommandResult;

public class CLIBuildCommand extends CLICommand {
    public CLIBuildCommand(final CLICommandOption opts) {
        super(opts);
    }

    @Override
    public void execute() {
        BuildCommandParams params = new BuildCommandParams();

        try {
            BuildCommandResult result = (BuildCommandResult) CoreServiceLoader.INSTANCE.getCoreService().runCommand(CommandType.BUILD, params);

            checkSuccess(result);
        } catch (CommandException e) {
            CLIErrorHandler.errorAndExit(String.format("Build failed with error message: %s", e.getErrorString()));
        }
    }

    private void checkSuccess(final BuildCommandResult result) {

    }
}
