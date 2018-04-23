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
     * 2. Create subdirectories `src` and `tests`
     * 3. Create the `Garvel.gl` file.
     * 4. Populate the Garvel.gl cache.
     * 5. Fetch dependencies and cache in .garvel, creating appropriate directories.
     *
     * @return
     * @throws Exception
     */
    @Override
    public NewCommandResult call() throws JobException {
        Path projectPath = null;
        try {
            projectPath = CoreModuleLoader.INSTANCE.getFileSystemFramework().makeDirectory(path);
        } catch (FilesystemFrameworkException e) {
            throw new JobException(String.format("Failed to create project directory \"%s\", %s",
                    path, e.getLocalizedMessage()));
        }

        final String srcDirPath = projectPath + File.separator + "src";
        Path srcPath = null;
        try {
            srcPath = CoreModuleLoader.INSTANCE.getFileSystemFramework().makeDirectory(srcDirPath);
        } catch (FilesystemFrameworkException e) {
            throw new JobException(String.format("Failed to create project \"src\" directory inside project root \"%s\", %s",
                    path, e.getLocalizedMessage()));
        }

        final String testsDirPath = projectPath + File.separator + "tests";
        Path testsPath = null;

        try {
            testsPath = CoreModuleLoader.INSTANCE.getFileSystemFramework().makeDirectory(testsDirPath);
        } catch (FilesystemFrameworkException e) {
            throw new JobException(String.format("Failed to create project \"tests\" directory inside project root \"%s\", %s",
                    path, e.getLocalizedMessage()));
        }

        final String configFilePath = projectPath + File.separator + "Garvel.gl";
        Path configPath = null;

        try {
            final String configString = CoreModuleLoader.INSTANCE.getFileSystemFramework().loadClassPathFileAsString(GarvelCoreConstants.GARVEL_CONFIG_TEMPLATE);
            configPath = CoreModuleLoader.INSTANCE.getFileSystemFramework().makeFileWithContents(configFilePath, configString);
        } catch (FilesystemFrameworkException e) {
            throw new JobException(String.format("Failed to create Garvel config file, \"Garvel.gl\" inside project root \"%s\", %s",
                    path, e.getLocalizedMessage()));
        }

        return new NewCommandResult(projectPath, srcPath, testsPath, configPath);
    }
}
