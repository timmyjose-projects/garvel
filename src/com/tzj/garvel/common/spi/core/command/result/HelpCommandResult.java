package com.tzj.garvel.common.spi.core.command.result;

import com.tzj.garvel.common.spi.core.command.CommandResult;

public class HelpCommandResult extends CommandResult {
    private String helpContents;

    public HelpCommandResult(final String helpContents) {

        this.helpContents = helpContents;
    }

    public String getHelpContents() {
        return helpContents;
    }
}
