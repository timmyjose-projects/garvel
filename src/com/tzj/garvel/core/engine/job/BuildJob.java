package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.BuildCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;

public class BuildJob implements Job<BuildCommandResult> {
    @Override
    public BuildCommandResult call() throws Exception {
        return null;
    }
}
