package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelUncheckedException;

public class SemverScannerException extends GarvelUncheckedException {
    public SemverScannerException(final String errorMessage) {
        super(errorMessage);
    }
}
