package com.tzj.garvel.core.engine.job;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

import com.tzj.garvel.common.spi.core.VCSType;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.result.InitCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.cache.CacheManagerServiceImpl;
import com.tzj.garvel.core.cache.exception.CacheManagerException;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

public class InitJob implements Job<InitCommandResult> {
    private VCSType vcs;
    private String currentDirectory;

    public InitJob(final VCSType vcs, final String currentDirectory) {
        this.vcs = vcs;
        this.currentDirectory = currentDirectory;
    }

    /**
     * 1. Create subdirectories `src`, `logs`, and`tests`
     * 2. Create the `Garvel.gl` file.
     * 3. Fetch dependencies and cache in .garvel, creating appropriate directories. @TODO
     *
     * @return
     * @throws Exception
     */
    @Override
    public InitCommandResult call() throws JobException {
        final Path srcPath = createPath("src");
        final Path testsPath = createPath("tests");
        final Path configPath = createConfigPath("Garvel.gl");

        return new InitCommandResult(srcPath, testsPath, configPath);
    }

    /**
     * Helper method to create the Garvel.gl config file.
     *
     * @param currentDirectory
     * @return
     * @throws JobException
     */
    private Path createConfigPath(final String currentDirectory) throws JobException {
        Path configFilePath = null;
        try {
            final String configFileTemplate = CoreModuleLoader.INSTANCE.getFileSystemFramework().loadClassPathFileAsString(GarvelCoreConstants.GARVEL_CONFIG_TEMPLATE);
            configFilePath = CoreModuleLoader.INSTANCE.getFileSystemFramework().makeFileWithContents(currentDirectory, configFileTemplate);
        } catch (FilesystemFrameworkException e) {
            throw new JobException(String.format("Failed to create Garvel config file, \"Garvel.gl\", %s", e.getErrorString()));
        }

        return configFilePath;
    }

    /**
     * Helper method to create the project skeleton.
     *
     * @param path
     * @return
     * @throws JobException
     */
    private Path createPath(final String path) throws JobException {
        Path createdPath = null;
        try {
            createdPath = CoreModuleLoader.INSTANCE.getFileSystemFramework().makeDirectory(path);
        } catch (FilesystemFrameworkException e) {
            throw new JobException(String.format("Failed to create project directory \"%s\", %s",
                    path, e.getLocalizedMessage()));
        }

        return createdPath;
    }
}
