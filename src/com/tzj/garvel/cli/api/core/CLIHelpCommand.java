package com.tzj.garvel.cli.api.core;

public class CLIHelpCommand extends CLICommand {
    private String commandName;

    public CLIHelpCommand(final CLICommandOption opts, final String commandName) {
        super(opts);
        this.commandName = commandName;
    }

    @Override
    public void execute() {

    }
}
