package com.tzj.garvel.cli.exception;

import com.tzj.garvel.cli.CLI;

/**
 * Construct useful messages from the Parser and/or Scanner exceptions.
 */
public class CLIErrorHandler {
    public static void errorAndExit(final String format) {
        System.err.println(format);
        System.out.printf("\nRun \"garvel list\" to see the list of installed commands\n");
        System.exit(1);
    }

    public static void exit(final String message) {
        System.out.println(message);
        CLI.displayUsageAndExit();
    }
}
