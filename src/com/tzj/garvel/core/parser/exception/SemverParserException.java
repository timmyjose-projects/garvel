package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class SemverParserException extends ParserException {
    public SemverParserException(final String errorMessage) {
        super(errorMessage);
    }
}
