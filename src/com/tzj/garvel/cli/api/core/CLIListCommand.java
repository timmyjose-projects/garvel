package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.CoreServiceLoader;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.ListCommandParams;
import com.tzj.garvel.common.spi.core.command.result.ListCommandResult;

import java.util.List;

public class CLIListCommand extends CLICommand {
    public CLIListCommand(final CLICommandOption opts) {
        super(opts);
    }

    @Override
    public void execute() {
        final ListCommandParams params = new ListCommandParams();
        try {
            final ListCommandResult commands = (ListCommandResult) CoreServiceLoader.INSTANCE.getCoreService().runCommand(CommandType.LIST, params);

            final List<String> validCommands = commands.getValidCommands();
            System.out.printf("Installed commands:\n");

            for (String command : validCommands) {
                System.out.printf("\t%s\n", command);
            }
        } catch (CommandException e) {
            CLIErrorHandler.errorAndExit("Unable to fetch list of available commands. Reason = " + e.getLocalizedMessage());
        }
    }
}