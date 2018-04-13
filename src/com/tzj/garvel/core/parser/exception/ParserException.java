package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class ParserException extends GarvelCheckedException {
    public ParserException(final String errorMessage) {
        super(errorMessage);
    }
}
