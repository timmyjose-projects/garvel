package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelUncheckedException;

public class ParserException extends GarvelUncheckedException {
    public ParserException(final String errorMessage) {
        super(errorMessage);
    }
}
