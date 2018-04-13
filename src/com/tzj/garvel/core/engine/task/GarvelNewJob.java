package com.tzj.garvel.core.engine.task;

import com.tzj.garvel.core.concurrent.spi.GarvelJob;

public class GarvelNewJob<GarvelNewCommandResult> implements GarvelJob<GarvelNewCommandResult> {
    @Override
    public GarvelNewCommandResult call() throws Exception {
        // logic
        return null;
    }
}
