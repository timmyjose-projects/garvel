package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.InitCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;

public class InitJob implements Job<InitCommandResult> {
    @Override
    public InitCommandResult call() throws Exception {
        return null;
    }
}
