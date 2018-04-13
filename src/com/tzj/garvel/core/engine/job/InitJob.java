package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.core.concurrent.spi.Job;

public class InitJob<GarvelInitCommandResult> implements Job<GarvelInitCommandResult> {
    @Override
    public GarvelInitCommandResult call() throws Exception {
        return null;
    }
}
