package com.tzj.garvel.cli;

import com.tzj.garvel.cli.api.core.CLICommand;
import com.tzj.garvel.cli.api.core.CLICoreService;
import com.tzj.garvel.cli.api.parser.CLIParser;
import com.tzj.garvel.cli.api.parser.ast.CLIAst;
import com.tzj.garvel.cli.exception.CLIErrorHandler;

/**
 * The Command Line Interface for Garvel.
 *
 * @author tzj
 */
public class CLI {
    /**
     * Entrypoint for Garvel (CLI).
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            CLIErrorHandler.displayUsageAndExit(0);
        }

        final CLIParser parser = ModuleLoader.INSTANCE.getParser();
        final CLIAst program = parser.parse(args);

        final CLICoreService service = ModuleLoader.INSTANCE.getCLICoreService();
        service.dispatchCommand(program);
        service.cleanup();
    }
}
