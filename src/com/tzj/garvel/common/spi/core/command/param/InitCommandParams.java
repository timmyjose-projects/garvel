package com.tzj.garvel.common.spi.core.command.param;

import com.tzj.garvel.common.spi.core.VCSType;
import com.tzj.garvel.common.spi.core.command.CommandParams;

public class InitCommandParams extends CommandParams {
    private VCSType vcs;
    private String currentDirectory;

    public InitCommandParams(final VCSType vcs, final String currentDirectory) {
        this.vcs = vcs;
        this.currentDirectory = currentDirectory;
    }

    public VCSType getVcs() {
        return vcs;
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }
}
