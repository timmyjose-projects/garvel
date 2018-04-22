package com.tzj.garvel.common.spi.core.command.param;

import com.tzj.garvel.common.spi.core.VCSType;
import com.tzj.garvel.common.spi.core.command.CommandParams;

public class NewCommandParams extends CommandParams {
    private VCSType vcs;
    private String path;

    public NewCommandParams(final VCSType vcs, final String path) {
        this.vcs = vcs;
        this.path = path;
    }

    public VCSType getVcs() {
        return vcs;
    }

    public String getPath() {
        return path;
    }
}
