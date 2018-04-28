package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.exception.GarvelCheckedException;

public class ParserException extends GarvelCheckedException {
    private static final long serialVersionUID = 2738695113787420094L;

    public ParserException(final String errorMessage) {
        super(errorMessage);
    }

    public ParserException() {
        super();
    }
}
