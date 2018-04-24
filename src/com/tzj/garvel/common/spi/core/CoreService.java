package com.tzj.garvel.common.spi.core;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.error.GarvelCheckedException;
import com.tzj.garvel.core.cache.exception.CacheManagerException;

/**
 * Represents the core of the garvel system.
 */
public interface CoreService {
    CommandResult runCommand(final CommandType cmd, final CommandParams cmdParams) throws CommandException;

    void invokeCachePopulation() throws CacheManagerException;

    void cleanup();
}
