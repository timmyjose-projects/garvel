package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.param.ListCommandParams;
import com.tzj.garvel.common.spi.core.command.result.ListCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;

import java.util.List;

public class ListJob implements Job<ListCommandResult> {
    private final ListCommandParams cmdParams;

    public ListJob(final ListCommandParams cmdParams) {
        this.cmdParams = cmdParams;
    }

    @Override
    public ListCommandResult call() throws JobException {
        final List<String> installedCommands = CoreModuleLoader.INSTANCE.getConfigManager().getInstalledCommands();
        return new ListCommandResult(installedCommands);
    }
}
