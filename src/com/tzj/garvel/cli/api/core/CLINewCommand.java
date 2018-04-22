package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.CoreServiceLoader;
import com.tzj.garvel.common.spi.core.VCSType;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.NewCommandParams;
import com.tzj.garvel.common.spi.core.command.result.NewCommandResult;

/**
 * Invoked the core command and process the results.
 * For now, the `--vcs` flag is a dummy flag. In
 * later versions, this flag will be used to
 * initialise the new project with the given
 * VCS.
 */
public class CLINewCommand extends CLICommand {
    private VCSType vcs;
    private String path;

    public CLINewCommand(final CLICommandOption opts, final VCSType vcs, final String path) {
        super(opts);
        this.vcs = vcs;
        this.path = path;
    }

    /**
     * Create the project structure:
     * 1. Create a directory with the given name.
     * 2. Create the Garvel.gl file inside.
     * 3. Create the `src`, and `tests` directories.
     * 4. Populate the Garvel.gl cache, and fetch data.
     */
    @Override
    public void execute() {
        NewCommandParams params = new NewCommandParams(vcs, path);
        try {
            NewCommandResult result = (NewCommandResult) CoreServiceLoader.INSTANCE.getCoreService().runCommand(CommandType.NEW, params);

            checkSuccess(result);

            System.out.println(String.format("Project \"%s\" created successfuily", path));
        } catch (CommandException e) {
            CLIErrorHandler.errorAndExit(String.format("Unable to create new Garvel project \"%s\". Reason = " + e.getLocalizedMessage()));
        }
    }

    private void checkSuccess(final NewCommandResult result) {
        if (result.getProjectPath().toFile().exists() && result.getSrcPath().toFile().exists() && result.getTestsPath().toFile().exists() && result.getConfigPath().toFile().exists()) {
            return;
        }

        CLIErrorHandler.errorAndExit(String.format("Unable to create new Garvel project \"%s\"", path));
    }
}
