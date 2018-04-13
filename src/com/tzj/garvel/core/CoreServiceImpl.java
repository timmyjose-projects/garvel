package com.tzj.garvel.core;

import com.tzj.garvel.common.spi.core.CoreService;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.command.BuildCommand;
import com.tzj.garvel.core.engine.command.CleanCommand;
import com.tzj.garvel.core.engine.command.InitCommand;
import com.tzj.garvel.core.engine.command.NewCommand;

/**
 * This class represents the core of the garvel package manager.
 * This acts as the interface (for running commands) between the
 * core engine and external clients.
 * Only one instance of core should be running at any point in time.
 */
public enum CoreServiceImpl implements CoreService {
    INSTANCE;

    @Override
    public CommandResult runCommand(final CommandType type, final CommandParams cmdParams) {
        Command command = null;

        switch (type) {
            case NEW:
                command = new NewCommand();
                break;
            case INIT:
                command = new InitCommand();
                break;
            case BUILD:
                command = new BuildCommand();
                break;
            case CLEAN:
                command = new CleanCommand();
                break;
            default:
                throw new IllegalStateException("TODO");
        }

        return command.execute(cmdParams);
    }
}
