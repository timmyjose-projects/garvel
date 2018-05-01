package com.tzj.garvel.common.spi.core.command.param;

import com.tzj.garvel.common.spi.core.command.CommandParams;

public class RunCommandParams extends CommandParams {
    private final String target;
    private String[] args;

    public RunCommandParams(final String target, final String[] args) {
        this.target = target;
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }

    public String getTarget() {
        return target;
    }
}
