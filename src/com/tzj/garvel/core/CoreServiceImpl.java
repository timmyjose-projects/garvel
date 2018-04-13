package com.tzj.garvel.core;

import com.tzj.garvel.common.spi.core.CoreService;
import com.tzj.garvel.common.spi.core.command.GarvelCommandParams;
import com.tzj.garvel.common.spi.core.command.GarvelCommandResult;
import com.tzj.garvel.common.spi.core.command.GarvelCommandType;
import com.tzj.garvel.core.engine.GarvelCommand;
import com.tzj.garvel.core.engine.command.GarvelBuildCommand;
import com.tzj.garvel.core.engine.command.GarvelCleanCommand;
import com.tzj.garvel.core.engine.command.GarvelInitCommand;
import com.tzj.garvel.core.engine.command.GarvelNewCommand;

/**
 * This class represents the core of the garvel package manager.
 * This acts as the interface (for running commands) between the
 * core engine and external clients.
 * Only one instance of core should be running at any point in time.
 */
public enum CoreServiceImpl implements CoreService {
    INSTANCE;

    @Override
    public GarvelCommandResult runCommand(final GarvelCommandType type, final GarvelCommandParams cmdParams) {
        GarvelCommand command = null;

        switch (type) {
            case NEW:
                command = new GarvelNewCommand();
                break;
            case INIT:
                command = new GarvelInitCommand();
                break;
            case BUILD:
                command = new GarvelBuildCommand();
                break;
            case CLEAN:
                command = new GarvelCleanCommand();
                break;
            default:
                throw new IllegalStateException("TODO");
        }

        return command.execute(cmdParams);
    }
}
