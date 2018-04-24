package com.tzj.garvel.cli.api.core;

public class CLIRunCommand extends CLICommand {
    private final String target;

    public CLIRunCommand(final CLICommandOption opts, final String target) {
        super(opts);
        this.target = target;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException();
    }
}
