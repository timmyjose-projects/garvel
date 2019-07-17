package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.CoreServiceLoader;
import com.tzj.garvel.common.spi.core.VCSType;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.InitCommandParams;
import com.tzj.garvel.common.spi.core.command.result.InitCommandResult;
import com.tzj.garvel.common.util.UtilServiceImpl;

public class CLIInitCommand extends CLICommand {
    private VCSType vcs;
    private final String currentDirectory;

    public CLIInitCommand(final CLICommandOption opts, final VCSType vcs, final String currentDirectory) {
        super(opts);
        this.vcs = vcs;
        this.currentDirectory = currentDirectory;
    }

    /**
     * Create the project structure inside the current directory:
     * 1. Create the Garvel.gl file inside. Fail if already exists.
     * 2. Create the `src`, and `tests` directories. Fail if already exists.
     * 3. Populate the Garvel.gl cache, and fetch data.
     */

    @Override
    public void execute() {
        final InitCommandParams params = new InitCommandParams(vcs, currentDirectory);

        try {
            final InitCommandResult result = (InitCommandResult) CoreServiceLoader.INSTANCE.getCoreService().runCommand(CommandType.INIT, params);

            if (result == null) {
                CLIErrorHandler.errorAndExit("init command failed: internal error");
            }

            checkSuccess(result);

            UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, "Project \"%s\" initialised successfully", currentDirectory);
        } catch (CommandException e) {
            CLIErrorHandler.errorAndExit("Unable to initialise new Garvel project \"%s\". Reason = %s", currentDirectory, e.getErrorString());
        }
    }

    private void checkSuccess(final InitCommandResult result) {
        if (result.getSrcPath().toFile().exists() &&
                result.getTestsPath().toFile().exists() &&
                result.getConfigPath().toFile().exists()) {
            return;
        }

        CLIErrorHandler.errorAndExit("Unable to initialise current directory as a Garvel project \"%s\"", currentDirectory);
    }
}