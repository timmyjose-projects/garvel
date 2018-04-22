package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.VersionCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;

public class VersionJob implements Job<VersionCommandResult> {
    @Override
    public VersionCommandResult call() throws JobException {
        final String version = CoreModuleLoader.INSTANCE.getConfigManager().getVersion();
        return new VersionCommandResult(version);
    }
}
