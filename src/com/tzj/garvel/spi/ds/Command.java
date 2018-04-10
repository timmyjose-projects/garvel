package com.tzj.garvel.spi.ds;

public interface Command {
    CommandResult  execute(CommandParams params);
}
