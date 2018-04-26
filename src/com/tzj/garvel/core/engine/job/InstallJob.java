package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.param.InstallCommandParams;
import com.tzj.garvel.common.spi.core.command.result.InstallCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.command.InstallCommand;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.filesystem.api.FilesystemService;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

import java.io.File;

public class InstallJob implements Job<InstallCommandResult> {
    private final InstallCommandParams cmdParams;

    public InstallJob(final InstallCommandParams cmdParams) {
        this.cmdParams = cmdParams;
    }

    /**
     * 0.Fetch the list of available artifacts from the Maven Central (and other) repositories and populate the local registry. @TODO
     * 1. Create the $HOME/.garvel directory and set R/W permissions.
     * 2. Create the $HOME/.garvel/cache directory and set R/W permissions.
     */
    @Override
    public InstallCommandResult call() throws JobException {
        final String garvelRoot = GarvelCoreConstants.GARVEL_HOME_DIR + File.separator + GarvelCoreConstants.GARVEL_DIR;
        final String garvelCache = garvelRoot + File.separator + GarvelCoreConstants.GARVEL_CACHE_DIR;

        final InstallCommandResult installResult = new InstallCommandResult();
        final FilesystemService fs = CoreModuleLoader.INSTANCE.getFileSystemFramework();

        if (fs.checkDirectoryExists(garvelRoot)) {
            installResult.setGarvelRoot(true);
        } else {
            try {
                fs.makeDirectory(garvelRoot);
                installResult.setGarvelRoot(true);
            } catch (FilesystemFrameworkException e) {
                cleanUp(fs, garvelRoot, garvelCache);
                throw new JobException(String.format("Failed to create the Garvel root directory, %s: %s\n", garvelRoot, e.getErrorString()));
            }
        }

        if (fs.checkDirectoryExists(garvelCache)) {
            installResult.setGarvelCache(true);
        } else {
            try {
                fs.makeDirectory(garvelCache);
                installResult.setGarvelCache(true);
            } catch (FilesystemFrameworkException e) {
                cleanUp(fs, garvelRoot, garvelCache);
                throw new JobException(String.format("Failed to create the Garvel cache directory, %s: %s\n", garvelCache, e.getErrorString()));
            }
        }

        return installResult;
    }

    /**
     * Ensure that any partially created directories are deleted.
     *
     * @param fs
     * @param garvelRoot
     * @param garvelCache
     */
    private void cleanUp(final FilesystemService fs, final String garvelRoot, final String garvelCache) {
        try {
            fs.deleteDirectory(garvelRoot);
            fs.deleteDirectory(garvelCache);
        } catch (Throwable err) {
            // do nothing. we shouldn't fail
            // because of this.
        }
    }
}
