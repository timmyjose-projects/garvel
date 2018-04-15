package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;

/**
 * This will simply dispatch the execution result to the Core
 * module.
 */
public interface CLICommand {
    void executeRemote();
}
