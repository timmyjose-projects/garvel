package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.ListCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;

import java.util.List;

public class ListJob implements Job<ListCommandResult> {
    @Override
    public ListCommandResult call() throws Exception {
        final List<String> installedCommands = CoreModuleLoader.INSTANCE.getConfigManager().getInstalledCommands();
        return new ListCommandResult(installedCommands);
    }
}
