package com.tzj.garvel.core;

import com.tzj.garvel.common.spi.core.CoreService;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.error.GarvelCheckedException;
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
            case INIT:
                command = new InitCommand();
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
     * 1. Check that the Garvel directory has been created (create it otherwise) and that access permissions
     * are valid.
     * 2. Parse the configuration file and populate the cache.
     */
    @Override
    public void setup() throws GarvelCheckedException {
        // ensure that the garvel directory already exists
        if (!checkGarvelDir()) {
            makeGarvelDir();
        }

        // parse the Garvel.gl file and populate the cache
        CoreModuleLoader.INSTANCE.getCacheManager().populateCache();

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

    @Override
    public void cleanup() {
        CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().shutdown();
    }
}
