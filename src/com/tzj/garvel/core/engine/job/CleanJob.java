package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.core.concurrent.api.Job;

public class CleanJob<GarvelCleanCommandResult> implements Job<GarvelCleanCommandResult> {
    @Override
    public GarvelCleanCommandResult call() throws Exception {
        return null;
    }
}
