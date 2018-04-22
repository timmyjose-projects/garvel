package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.TestCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;

public class TestJob implements Job<TestCommandResult> {
    @Override
    public TestCommandResult call() throws JobException {
        return null;
    }
}
