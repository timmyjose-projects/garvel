package com.tzj.garvel.cli.core;

import com.tzj.garvel.cli.api.core.*;
import com.tzj.garvel.cli.api.parser.ast.*;
import com.tzj.garvel.common.spi.core.CoreServiceLoader;

/**
 * This class performs the conceptual mapping from the CLI commands to the core commands.
 */
public enum CLICoreServiceImpl implements CLICoreService {
    INSTANCE;

    /**
     * Dispatch the appropriate command to Core.
     *
     * @param ast
     */
    @Override
    public void dispatchCommand(final CLIAst ast) {
        final Program program = (Program) ast;
        program.accept(new CLIAstCommandDispatchVisitor());
    }

    /**
     * Perform clean-up before exiting. Currently, all this does is
     * to call the core to shutdown the Job Engine.
     */
    @Override
    public void cleanup() {
        CoreServiceLoader.INSTANCE.getCoreService().cleanup();
    }
}
