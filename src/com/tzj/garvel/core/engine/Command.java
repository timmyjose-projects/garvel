package com.tzj.garvel.core.engine;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;

public interface Command {
    CommandResult execute(CommandParams params) throws CommandException;
}
