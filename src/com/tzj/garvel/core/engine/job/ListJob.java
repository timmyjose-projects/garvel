package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.ListCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;

public class ListJob implements Job<ListCommandResult> {
    @Override
    public ListCommandResult call() throws Exception {
        return null;
    }
}
