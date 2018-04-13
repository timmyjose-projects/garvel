package com.tzj.garvel.core.engine;

import com.tzj.garvel.common.spi.core.command.GarvelCommandParams;
import com.tzj.garvel.common.spi.core.command.GarvelCommandResult;

public interface GarvelCommand {
    GarvelCommandResult execute(GarvelCommandParams params);
}
