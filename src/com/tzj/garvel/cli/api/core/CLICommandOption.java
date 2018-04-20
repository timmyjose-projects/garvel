package com.tzj.garvel.cli.api.core;

public class CLICommandOption {
    private boolean verbose;
    private boolean quiet;

    public CLICommandOption(final boolean verbose, final boolean quiet) {
        this.verbose = verbose;
        this.quiet = quiet;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public boolean isQuiet() {
        return quiet;
    }
}
