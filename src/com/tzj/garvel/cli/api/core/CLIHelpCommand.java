package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.CoreServiceLoader;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.HelpCommandParams;
import com.tzj.garvel.common.spi.core.command.result.HelpCommandResult;
import com.tzj.garvel.common.util.UtilServiceImpl;

public class CLIHelpCommand extends CLICommand {
    private String commandName;

    public CLIHelpCommand(final CLICommandOption opts, final String commandName) {
        super(opts);
        this.commandName = commandName;
    }

    /**
     * Fetch the formatted help page for the specified valid command.
     */
    @Override
    public void execute() {
        HelpCommandParams params = new HelpCommandParams(commandName);
        try {
            HelpCommandResult result = (HelpCommandResult) CoreServiceLoader.INSTANCE.getCoreService().runCommand(CommandType.HELP, params);

            if (result == null) {
                CLIErrorHandler.errorAndExit("help command failed: internal error");
            }

            UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, result.getHelpContents());
        } catch (CommandException e) {
            CLIErrorHandler.errorAndExit("Unable to display help for \"%s\". Reason = %s\n", commandName, e.getLocalizedMessage());
        }
    }
}
