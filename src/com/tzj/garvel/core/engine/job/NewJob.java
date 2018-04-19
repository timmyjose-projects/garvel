package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.result.NewCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;

public class NewJob implements Job<NewCommandResult> {
    @Override
    public NewCommandResult call() throws Exception {
        // logic
        return null;
    }
}
