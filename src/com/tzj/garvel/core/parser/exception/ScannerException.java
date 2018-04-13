package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class ScannerException extends GarvelCheckedException {
    public ScannerException(final String errorMessage) {
        super(errorMessage);
    }
}
