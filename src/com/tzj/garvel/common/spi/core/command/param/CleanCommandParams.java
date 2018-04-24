package com.tzj.garvel.common.spi.core.command.param;

import com.tzj.garvel.common.spi.core.command.CommandParams;

public class CleanCommandParams extends CommandParams {
    private final boolean includeLogs;

    public CleanCommandParams(final boolean includeLogs) {
        this.includeLogs = includeLogs;
    }

    public boolean isIncludeLogs() {
        return includeLogs;
    }
}
