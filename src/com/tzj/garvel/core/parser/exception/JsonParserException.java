package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class JsonParserException extends ParserException {
    private static final long serialVersionUID = 8767961345948156722L;

    public JsonParserException(final String errorMessage) {
        super(errorMessage);
    }
}
