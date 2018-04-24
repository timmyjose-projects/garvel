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
            "\t-v, --verbose      Use verbose output (default)\n" +
            "\t-q, --quiet        No output printed to stdout\n\n" +
            "Note that you can specify either `--verbose` or `--quiet` but not both.\n\n" +
            "Some common garvel commands are (see all commands with --list):\n" +
            "\thelp        Display help for a specific command\n" +
            "\tversion     Display version information\n" +
            "\tlist        List all the available commands\n" +
            "\tinstall     Setup Garvel\n" +
            "\tuninstall   Remove Garvel setup\n" +
            "\tnew         Create a new Garvel project\n" +
            "\tbuild       Compile the current project and generate artifacts\n" +
            "\tclean       Remove the target directory\n" +
            "\trun         Build and execute the specified target\n" +
            "\tupdate      Fetch the latest registry from Maven Central (and other repos)\n" +
            "\tdep         Display the available versions and dependencies of the specified artifact\n\n" +

            "See 'garvel help <command>' for more information on a specific command.\n\n";

    /**
     * Entrypoint for Garvel (CLI).
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            displayUsageAndExit();
        }

        final CLIParser parser = ModuleLoader.INSTANCE.getParser();
        final CLIAst program = parser.parse(args);

        final CLICoreService service = ModuleLoader.INSTANCE.getCLICoreService();
        service.dispatchCommand(program);
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
