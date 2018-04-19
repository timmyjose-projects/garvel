package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelUncheckedException;

public class SemverScannerException extends GarvelUncheckedException {
    private static final long serialVersionUID = 7603059000694453837L;

    public SemverScannerException(final String errorMessage) {
        super(errorMessage);
    }
}
