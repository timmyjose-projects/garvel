package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.VersionCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;

public class VersionJob implements Job<VersionCommandResult> {
    @Override
    public VersionCommandResult call() throws Exception {
        final String version = CoreModuleLoader.INSTANCE.getConfigManager().getVersion();
        return new VersionCommandResult(version);
    }
}
