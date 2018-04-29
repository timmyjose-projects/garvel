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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UninstallJob implements Job<UninstallCommandResult> {
    private final UninstallCommandParams cmdParams;

    public UninstallJob(final UninstallCommandParams cmdParams) {
        this.cmdParams = cmdParams;
    }

    @Override
    public UninstallCommandResult call() throws JobException {
        final Path garvelPath = Paths.get(GarvelCoreConstants.GARVEL_DIR);

        final UninstallCommandResult uninstallResult = new UninstallCommandResult();
        final FilesystemService fs = CoreModuleLoader.INSTANCE.getFileSystemFramework();

        try {
            fs.deleteDirectoryHierarchy(garvelPath);
            uninstallResult.setGarvelRoot(fs.checkDirectoryExists(GarvelCoreConstants.GARVEL_DIR));
            uninstallResult.setGarvelCache(fs.checkDirectoryExists(GarvelCoreConstants.GARVEL_CACHE_DIR));
        } catch (FilesystemFrameworkException e) {
            throw new JobException(String.format("Unable to uninstall Garvel: %s", e.getErrorString()));
        }

        return uninstallResult;
    }
}
