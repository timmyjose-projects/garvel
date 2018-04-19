package com.tzj.garvel.cli.exception;

import com.tzj.garvel.cli.parser.scanner.lexer.CLILexer;
import com.tzj.garvel.common.spi.error.GarvelUncheckedException;

public class CLILexerException extends GarvelUncheckedException {
    private static final long serialVersionUID = 4854153533628293757L;

    public CLILexerException(final String message) {
        super(message);
    }
}
