package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelUncheckedException;

public class LexerException extends GarvelUncheckedException {
    public LexerException(final String errorMessage) {
        super(errorMessage);
    }
}
