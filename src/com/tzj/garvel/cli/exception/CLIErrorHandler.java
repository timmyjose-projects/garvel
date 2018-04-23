package com.tzj.garvel.cli.exception;

import com.tzj.garvel.cli.CLI;

/**
 * Construct useful messages from the Parser and/or Scanner exceptions.
 */
public class CLIErrorHandler {
    public static void errorAndExit(final String format, final Object... values) {
        System.err.println(String.format(format, values));
        System.exit(1);
    }

    public static void exit(final String message) {
        System.out.println(message);
        CLI.displayUsageAndExit();
    }
}
