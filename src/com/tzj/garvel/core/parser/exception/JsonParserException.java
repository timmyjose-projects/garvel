package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class JsonParserException extends ParserException {
    public JsonParserException(final String errorMessage) {
        super(errorMessage);
    }
}
