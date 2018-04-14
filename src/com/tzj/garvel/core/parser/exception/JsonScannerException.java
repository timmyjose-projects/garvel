package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class JsonScannerException extends GarvelCheckedException {
    public JsonScannerException(final String errorMessage) {
        super(errorMessage);
    }
}
