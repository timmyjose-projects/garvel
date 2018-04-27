package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.DepCommandParams;
import com.tzj.garvel.common.spi.core.command.result.DepCommandResult;
import com.tzj.garvel.common.util.UtilServiceImpl;
import com.tzj.garvel.core.CoreServiceImpl;

public class CLIDepCommand extends CLICommand {
    private final String dependencyName;
    private final boolean showDependencies;

    public CLIDepCommand(final CLICommandOption opts, final String dependencyName, final boolean showDependencies) {
        super(opts);
        this.dependencyName = dependencyName;
        this.showDependencies = showDependencies;
    }

    @Override
    public void execute() {
        final DepCommandParams params = new DepCommandParams(dependencyName, showDependencies);
        try {
            final DepCommandResult result = (DepCommandResult) CoreServiceImpl.INSTANCE.runCommand(CommandType.DEP, params);
        } catch (CommandException e) {
            CLIErrorHandler.errorAndExit("Unable to resolve dependencies for %s. Reason = %s", dependencyName, e.getErrorString());
        }
    }
}
