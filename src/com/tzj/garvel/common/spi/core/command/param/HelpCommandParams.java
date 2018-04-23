package com.tzj.garvel.common.spi.core.command.param;

import com.tzj.garvel.common.spi.core.command.CommandParams;

public class HelpCommandParams extends CommandParams {
    private final String commandName;

    public HelpCommandParams(final String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
