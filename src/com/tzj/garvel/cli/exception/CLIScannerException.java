package com.tzj.garvel.cli.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class CLIScannerException extends GarvelCheckedException {
    private static final long serialVersionUID = 2898166623851021081L;

    public CLIScannerException(final String errorMessage) {
        super(errorMessage);
    }
}
