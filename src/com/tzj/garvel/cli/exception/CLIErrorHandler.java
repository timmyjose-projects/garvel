package com.tzj.garvel.cli.exception;

import com.tzj.garvel.cli.CLI;

/**
 * Construct useful messages from the Parser and/or Scanner exceptions.
 */
public class CLIErrorHandler {
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
            "\tdep         Display the available versions and dependencies of the specified artifact\n\n" +

            "See 'garvel help <command>' for more information on a specific command.\n\n";

    public static void errorAndExit(final String format, final Object... values) {
        System.err.println(String.format(format, values));
        System.exit(1);
    }

    public static void displayUsageAndExit(int exitCode) {
        System.out.println(USAGE);
        System.exit(exitCode);
    }
}
