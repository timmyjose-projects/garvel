package com.tzj.garvel.common.spi.core.command.result;

import com.tzj.garvel.common.spi.core.command.CommandResult;

public class UninstallCommandResult extends CommandResult {
    private boolean garvelRoot;
    private boolean garvelCache;

    public boolean isGarvelRoot() {
        return garvelRoot;
    }

    public void setGarvelRoot(final boolean garvelRoot) {
        this.garvelRoot = garvelRoot;
    }

    public boolean isGarvelCache() {
        return garvelCache;
    }

    public void setGarvelCache(final boolean garvelCache) {
        this.garvelCache = garvelCache;
    }
}
