package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.param.UninstallCommandParams;
import com.tzj.garvel.common.spi.core.command.result.InstallCommandResult;
import com.tzj.garvel.common.spi.core.command.result.UninstallCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.filesystem.api.FilesystemService;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

import java.io.File;

public class UninstallJob implements Job<UninstallCommandResult> {
    private final UninstallCommandParams cmdParams;

    public UninstallJob(final UninstallCommandParams cmdParams) {
        this.cmdParams = cmdParams;
    }

    @Override
    public UninstallCommandResult call() throws Exception {
        final String garvelRoot = GarvelCoreConstants.GARVEL_HOME_DIR + File.separator + GarvelCoreConstants.GARVEL_DIR;
        final String garvelCache = garvelRoot + File.separator + GarvelCoreConstants.GARVEL_CACHE_DIR;

        final UninstallCommandResult uninstallResult = new UninstallCommandResult();
        final FilesystemService fs = CoreModuleLoader.INSTANCE.getFileSystemFramework();

        //@TODO - Create a Visitor that will delete the hierarachy recursively.
        if (!fs.checkDirectoryExists(garvelCache)) {
            uninstallResult.setGarvelCache(true);
        } else {
            try {
                fs.deleteDirectory(garvelCache);
                uninstallResult.setGarvelCache(true);
            } catch (FilesystemFrameworkException e) {
                throw new JobException(String.format("Failed to delete the Garvel cache directory, %s: %s\n", garvelCache, e.getErrorString()));
            }
        }

        // the root has to be deleted last
        if (!fs.checkDirectoryExists(garvelRoot)) {
            uninstallResult.setGarvelRoot(true);
        } else {
            try {
                fs.deleteDirectory(garvelRoot);
                uninstallResult.setGarvelRoot(true);
            } catch (FilesystemFrameworkException e) {
                throw new JobException(String.format("Failed to delete the Garvel root directory, %s: %s\n", garvelRoot, e.getErrorString()));
            }
        }

        return uninstallResult;
    }
}
