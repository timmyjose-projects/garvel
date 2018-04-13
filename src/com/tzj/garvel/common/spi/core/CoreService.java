package com.tzj.garvel.common.spi.core;

import com.tzj.garvel.common.spi.core.command.GarvelCommandParams;
import com.tzj.garvel.common.spi.core.command.GarvelCommandResult;
import com.tzj.garvel.common.spi.core.command.GarvelCommandType;

/**
 * Represents the core of the garvel system.
 */
public interface CoreService {
    GarvelCommandResult runCommand(final GarvelCommandType cmd, final GarvelCommandParams cmdParams);
}
