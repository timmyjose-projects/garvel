package com.tzj.garvel.cli.core;

import com.tzj.garvel.cli.ModuleLoader;
import com.tzj.garvel.cli.api.core.*;
import com.tzj.garvel.cli.api.parser.ast.*;
import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.CoreServiceLoader;
import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public enum CLICoreServiceImpl implements CLICoreService {
    INSTANCE;

    @Override
    public CLICommand getCommand(final CLIAst ast) {
        Program program = (Program) ast;

        final CLICommandOption opts = new CLICommandOption(program.isVerbose(), program.isQuiet());
        final CommandAst command = program.getCommand();
        CLICommand cliCommand = null;

        if (command instanceof HelpCommandAst) {
            final HelpCommandAst cmd = (HelpCommandAst) command;
            cliCommand = new CLIHelpCommand(opts, cmd.getCommandName().getId().spelling());
        } else if (command instanceof ListCommandAst) {
            cliCommand = new CLIListCommand(opts);
        } else if (command instanceof VersionCommandAst) {
            cliCommand = new CLIVersionCommand(opts);
        } else if (command instanceof NewCommandAst) {
            final NewCommandAst newCommand = (NewCommandAst) command;
            cliCommand = new CLINewCommand(opts,
                    ModuleLoader.INSTANCE.getUtils().getVCSTypeFromString(newCommand.getVcs().getId().spelling()),
                    newCommand.getPath().getId().spelling());
        } else if (command instanceof BuildCommandAst) {
            cliCommand = new CLIBuildCommand(opts);
        } else if (command instanceof CleanCommandAst) {
            cliCommand = new CLICleanCommand(opts);
        } else if (command instanceof RunCommandAst) {
            cliCommand = new CLIRunCommand(opts);
        } else if (command instanceof TestCommandAst) {
            cliCommand = new CLITestCommand(opts);
        }

        return cliCommand;
    }

    /**
     * Contact the core service and ensure that all setup is done and ready.
     */
    @Override
    public void checkGarveEssentials() {
        try {
            CoreServiceLoader.INSTANCE.getCoreService().setup();
        } catch (GarvelCheckedException e) {
            CLIErrorHandler.exit("Garvel setup failed.");
        }
    }

    @Override
    public void cleanup() {
        CoreServiceLoader.INSTANCE.getCoreService().cleanup();
    }
}
