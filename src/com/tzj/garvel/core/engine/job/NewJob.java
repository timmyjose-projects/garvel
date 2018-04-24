package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.VCSType;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.result.NewCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.cache.CacheManagerServiceImpl;
import com.tzj.garvel.core.cache.exception.CacheManagerException;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

import java.io.File;
import java.nio.file.Path;

public class NewJob implements Job<NewCommandResult> {
    private VCSType vcs;
    private String path;

    public NewJob(final VCSType vcs, final String path) {
        this.vcs = vcs;
        this.path = path;
    }

    /**
     * 1. Create the project directory
     * 2. Create subdirectories `src`, `logs`, and`tests`
     * 3. Create the `Garvel.gl` file.
     * 4. Fetch dependencies and cache in .garvel, creating appropriate directories. @TODO
     *
     * @return
     * @throws Exception
     */
    @Override
    public NewCommandResult call() throws JobException {
        final Path projectPath = createPath(path);
        final Path srcPath = createPath(projectPath + File.separator + "src");
        final Path testsPath = createPath(projectPath + File.separator + "tests");
        final Path logsPath = createPath(projectPath + File.separator + "logs");
        final Path configPath = createConfigPath(projectPath + File.separator + "Garvel.gl");

        return new NewCommandResult(projectPath, srcPath, testsPath, logsPath, configPath);
    }

    /**
     * Helper method to create the Garvel.gl config file.
     *
     * @param path
     * @return
     * @throws JobException
     */
    private Path createConfigPath(final String path) throws JobException {
        Path configFilePath = null;
        try {
            final String configFileTemplate = CoreModuleLoader.INSTANCE.getFileSystemFramework().loadClassPathFileAsString(GarvelCoreConstants.GARVEL_CONFIG_TEMPLATE);
            configFilePath = CoreModuleLoader.INSTANCE.getFileSystemFramework().makeFileWithContents(path, configFileTemplate);
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
