package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.HelpCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;

public class HelpJob implements Job<HelpCommandResult> {
    @Override
    public HelpCommandResult call() throws JobException {
        return null;
    }
}
