package com.tzj.garvel.common.spi.core.command.result;

import com.tzj.garvel.common.spi.core.command.CommandResult;

import java.util.List;

public class ListCommandResult extends CommandResult {
    private List<String> validCommands;

    public ListCommandResult(final List<String> validCommands) {
        this.validCommands = validCommands;
    }

    public List<String> getValidCommands() {
        return validCommands;
    }
}
