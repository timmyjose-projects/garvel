package com.tzj.garvel.cli;

import com.tzj.garvel.cli.api.core.CLICommand;
import com.tzj.garvel.cli.api.core.CLICoreService;
import com.tzj.garvel.cli.api.parser.CLIParser;
import com.tzj.garvel.cli.api.parser.ast.CLIAst;

/**
 * The Command Line Interface for Garvel.
 *
 * @author tzj
 */
public class CLI {
    private static final String USAGE = "A Java package manager and dependency manager\n\n" +
            "Usage:\n" +
            "\tgarvel [OPTIONS] [COMMAND]\n\n" +
            "Options:\n" +
            "\t-v, --verbose       Use verbose output\n" +
            "\t -q, --quiet         No output printed to stdout (default)\n\n" +
            "Note that you can specify either `--verbose` or `--quiet` but not both.\n\n" +
            "Some common garvel commands are (see all commands with --list):" +
            "\thelp        Display this help and exit\n" +
            "\tversion     Display version info and exit\n" +
            "\tlist        List all the available commands\n" +
            "\tbuild       Compile the current project\n" +
            "\tclean       Remove the target directory\n" +
            "\tnew         Create a new garvel project\n" +
            "\trun         Build and execute src/Main.java\n" +
            "\ttest        Run the tests\n\n" +

            "See 'garvel help <command>' for more information on a specific command.\n\n";

    /**
     * Entrypoint for Garvel (CLI).
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Here");
            displayUsageAndExit();
        }

        final CLIParser parser = ModuleLoader.INSTANCE.getParser();
        final CLIAst program = parser.parse(args);

        final CLICoreService service = ModuleLoader.INSTANCE.getCLICoreService();
        service.checkGarveEssentials();
        final CLICommand command = service.getCommand(program);

        command.execute();
        service.cleanup();
    }

    /**
     * Print the usage message and exit with error code 1.
     */
    public static void displayUsageAndExit() {
        System.err.println(USAGE);
        System.exit(1);
    }
}
