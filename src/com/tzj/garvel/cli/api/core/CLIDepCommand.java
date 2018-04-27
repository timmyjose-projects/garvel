package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.DepCommandParams;
import com.tzj.garvel.common.spi.core.command.result.DepCommandResult;
import com.tzj.garvel.common.util.UtilServiceImpl;
import com.tzj.garvel.core.CoreServiceImpl;

public class CLIDepCommand extends CLICommand {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final boolean showDependencies;

    public CLIDepCommand(final CLICommandOption opts, final String groupId, final String artifactId, final String version, final boolean showDependencies) {
        super(opts);
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.showDependencies = showDependencies;
    }

    @Override
    public void execute() {
        final DepCommandParams params = new DepCommandParams(groupId, artifactId, version, showDependencies);
        try {
            final DepCommandResult result = (DepCommandResult) CoreServiceImpl.INSTANCE.runCommand(CommandType.DEP, params);

            UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, "The available versions are:\n", result.getVersions());

            if (result.isDependenciesInformationAvailable()) {
                UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, "The dependency tree for version %s is:\n",
                        version, result.getDependencyGraphString());
            }
        } catch (CommandException e) {
            CLIErrorHandler.errorAndExit("Unable to resolve dependencies for %s. Reason = %s", groupId + "/" + artifactId, e.getErrorString());
        }
    }
}
