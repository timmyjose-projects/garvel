package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class SemverScannerException extends GarvelCheckedException {
    public SemverScannerException(final String errorMessage) {
        super(errorMessage);
    }
}
