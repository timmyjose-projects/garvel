package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelUncheckedException;

public class JsonScannerException extends GarvelUncheckedException {
    private static final long serialVersionUID = 8506491309328984625L;

    public JsonScannerException(final String errorMessage) {
        super(errorMessage);
    }
}
