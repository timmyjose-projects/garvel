package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class LexerException extends GarvelCheckedException {
    private static final long serialVersionUID = 5264263596082669780L;

    public LexerException(final String errorMessage) {
        super(errorMessage);
    }
}
