package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.param.CleanCommandParams;
import com.tzj.garvel.common.spi.core.command.result.CleanCommandResult;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.engine.job.visitors.CleanJobVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CleanJob implements Job<CleanCommandResult> {
    private final CleanCommandParams cmdParams;

    public CleanJob(final CleanCommandParams cmdParams) {
        this.cmdParams = cmdParams;
    }

    /**
     * 1. Check if the target directory exists and if not, return true immediately.
     * 2. Walk the target directory and delete all the entries inside.
     * 3. Delete the target directory itself.
     *
     * @return
     * @throws JobException
     */
    @Override
    public CleanCommandResult call() throws JobException {
        final String targetDirFullName = GarvelCoreConstants.GARVEL_PROJECT_ROOT + File.separator + "target";
        final Path targetDirPath = Paths.get(targetDirFullName);

        if (targetDirPath.toFile().exists()) {
            try {
                Files.walkFileTree(targetDirPath, new CleanJobVisitor());
            } catch (IOException e) {
                throw new JobException(String.format("clean job failed to delete some files in the target directory: %s", e.getLocalizedMessage()));
            }
        }

        return new CleanCommandResult(true);
    }
}
