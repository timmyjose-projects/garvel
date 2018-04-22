package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.RunCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;

public class RunJob implements Job<RunCommandResult> {
    @Override
    public RunCommandResult call() throws JobException {
        return null;
    }
}
