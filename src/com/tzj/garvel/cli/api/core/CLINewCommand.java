package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.common.spi.core.VCSType;

/**
 * Invoked the core command and process the results.
 */
public class CLINewCommand extends CLICommand {
    private VCSType vcs;
    private boolean bin;
    private boolean lib;
    private String path;

    public CLINewCommand(final CLICommandOption opts, final VCSType vcs, final boolean bin, final boolean lib, final String path) {
        super(opts);
        this.vcs = vcs;
        this.bin = bin;
        this.lib = lib;
        this.path = path;
    }

    @Override
    public void execute() {

    }
}
