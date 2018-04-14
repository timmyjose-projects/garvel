package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelUncheckedException;

public class JsonScannerException extends GarvelUncheckedException {
    public JsonScannerException(final String errorMessage) {
        super(errorMessage);
    }
}
