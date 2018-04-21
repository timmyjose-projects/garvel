package com.tzj.garvel.core.parser.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class TOMLScannerException extends GarvelCheckedException {
    private static final long serialVersionUID = -1169457488273368998L;

    public TOMLScannerException(final String message) {
        super(message);
    }
}
