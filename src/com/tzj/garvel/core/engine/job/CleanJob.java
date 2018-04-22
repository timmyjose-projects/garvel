package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.CleanCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;

public class CleanJob implements Job<CleanCommandResult> {
    @Override
    public CleanCommandResult call() throws JobException {
        return null;
    }
}
