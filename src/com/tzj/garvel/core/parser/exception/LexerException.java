package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelUncheckedException;

public class LexerException extends GarvelUncheckedException {
    private static final long serialVersionUID = 5264263596082669780L;

    public LexerException(final String errorMessage) {
        super(errorMessage);
    }
}
