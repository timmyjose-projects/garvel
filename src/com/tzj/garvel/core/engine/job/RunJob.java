package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.RunCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;

public class RunJob implements Job<RunCommandResult> {
    @Override
    public RunCommandResult call() throws Exception {
        return null;
    }
}
