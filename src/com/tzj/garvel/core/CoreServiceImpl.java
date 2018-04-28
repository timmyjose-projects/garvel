package com.tzj.garvel.core;

import com.tzj.garvel.common.spi.core.CoreService;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.core.cache.exception.CacheManagerException;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.command.*;

import java.io.File;

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
            case INSTALL:
                command = new InstallCommand();
                break;
            case UNINSTALL:
                command = new UninstallCommand();
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
            case DEP:
                command = new DepCommand();
                break;
            case TEST:
                command = new TestCommand();
                break;
            default:
                throw new CommandException(String.format("Command %s is not a valid command", type));
        }

        return command.run(cmdParams);
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