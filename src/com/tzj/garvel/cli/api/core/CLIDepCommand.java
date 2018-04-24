package com.tzj.garvel.cli.api.core;

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

    }
}
