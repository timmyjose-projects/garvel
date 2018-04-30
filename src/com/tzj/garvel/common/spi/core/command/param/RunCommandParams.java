package com.tzj.garvel.common.spi.core.command.param;

import com.tzj.garvel.common.spi.core.command.CommandParams;

public class RunCommandParams extends CommandParams {
    private final String target;

    public RunCommandParams(final String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }
}
