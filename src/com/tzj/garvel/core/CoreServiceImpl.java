package com.tzj.garvel.core;

import com.tzj.garvel.common.spi.core.CoreService;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.error.GarvelCheckedException;
import com.tzj.garvel.core.cache.exception.CacheManagerException;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.command.*;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

import java.io.File;
import java.nio.file.Paths;

/**
 * This class represents the core of the garvel package manager.
 * This acts as the interface (for running commands) between the
 * core engine and external clients.
 * Only one instance of core should be running at any point in time.
 */
public enum CoreServiceImpl implements CoreService {
    INSTANCE;

    /**
     * Dispatch the request to the appropriate job to be executed by the Job Engine.
     *
     * @param type
     * @param cmdParams
     * @return
     * @throws CommandException
     */
    @Override
    public CommandResult runCommand(final CommandType type, final CommandParams cmdParams) throws CommandException {
        Command command = null;

        switch (type) {
            case HELP:
                command = new HelpCommand();
                break;
            case VERSION:
                command = new VersionCommand();
                break;
            case LIST:
                command = new ListCommand();
                break;
            case NEW:
                command = new NewCommand();
                break;
            case BUILD:
                command = new BuildCommand();
                break;
            case CLEAN:
                command = new CleanCommand();
                break;
            case RUN:
                command = new RunCommand();
                break;
            default:
                throw new CommandException(String.format("Command %s is not a valid command", type));
        }

        return command.execute(cmdParams);
    }

    /**
     * Check that the Garvel directory has been created (create it otherwise) and that access permissions
     * are valid.
     */
    @Override
    public void setup() throws GarvelCheckedException {
        // ensure that the garvel directory already exists
        if (!checkGarvelDir()) {
            makeGarvelDir();
        }
    }


    private void makeGarvelDir() throws FilesystemFrameworkException {
        final String fullPath = GarvelCoreConstants.GARVEL_HOME_DIR + File.separator + GarvelCoreConstants.GARVEL_DIR;
        CoreModuleLoader.INSTANCE.getFileSystemFramework().makeDirectory(fullPath);
    }

    private boolean checkGarvelDir() throws GarvelCheckedException {
        if (GarvelCoreConstants.GARVEL_HOME_DIR == null) {
            throw new GarvelCheckedException("Unable to resolve user's home directory. Aborting...");
        }

        final String fullPath = GarvelCoreConstants.GARVEL_HOME_DIR + File.separator + GarvelCoreConstants.GARVEL_DIR;
        return CoreModuleLoader.INSTANCE.getFileSystemFramework().checkDirectoryExists(fullPath);
    }

    /**
     * This will be called by the `new`, `build` and `run` commands to ensure that dependencies are up-to-date
     * before running the command.
     *
     * @throws CacheManagerException
     */
    @Override
    public void invokeCachePopulation() throws CacheManagerException {
        final String garvelConfigFile = GarvelCoreConstants.GARVEL_PROJECT_ROOT
                + File.separator
                + GarvelCoreConstants.GARVEL_CONFIG_FILE;

        if (CoreModuleLoader.INSTANCE.getFileSystemFramework().checkFileExists(garvelConfigFile)) {
            CoreModuleLoader.INSTANCE.getCacheManager().populateCache();
        } else {
            throw new CacheManagerException(String.format("Garvel configuration file %s does not exist!", garvelConfigFile));
        }
    }

    /**
     * Shut the ExecutorSevice (the Job Engine) down. Other daemon services could be
     * added here in later versions.
     */
    @Override
    public void cleanup() {
        CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().shutdown();
    }
}