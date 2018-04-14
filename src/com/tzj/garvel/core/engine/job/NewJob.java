package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.core.concurrent.api.Job;

public class NewJob<GarvelNewCommandResult> implements Job<GarvelNewCommandResult> {
    @Override
    public GarvelNewCommandResult call() throws Exception {
        // logic
        return null;
    }
}
