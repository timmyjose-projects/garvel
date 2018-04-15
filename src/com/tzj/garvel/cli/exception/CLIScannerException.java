package com.tzj.garvel.cli.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class CLIScannerException extends GarvelCheckedException {
    public CLIScannerException(final String errorMessage) {
        super(errorMessage);
    }
}
