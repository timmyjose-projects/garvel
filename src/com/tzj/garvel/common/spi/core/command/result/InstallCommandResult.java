package com.tzj.garvel.common.spi.core.command.result;

import com.tzj.garvel.common.spi.core.command.CommandResult;

public class InstallCommandResult extends CommandResult {
    private boolean garvelRoot;
    private boolean garvelRegistry;
    private boolean garvelCache;

    public boolean getGarvelRoot() {
        return garvelRoot;
    }

    public void setGarvelRoot(final boolean garvelRoot) {
        this.garvelRoot = garvelRoot;
    }

    public boolean getGarvelRegistry() {
        return garvelRegistry;
    }

    public void setGarvelRegistry(final boolean garvelRegistry) {
        this.garvelRegistry = garvelRegistry;
    }

    public boolean getGarvelCache() {
        return garvelCache;
    }

    public void setGarvelCache(final boolean garvelCache) {
        this.garvelCache = garvelCache;
    }
}
