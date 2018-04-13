package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.core.concurrent.spi.Job;

public class NewJob<GarvelNewCommandResult> implements Job<GarvelNewCommandResult> {
    @Override
    public GarvelNewCommandResult call() throws Exception {
        // logic
        return null;
    }
}
