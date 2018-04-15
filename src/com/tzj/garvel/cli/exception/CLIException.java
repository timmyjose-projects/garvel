package com.tzj.garvel.cli.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class CLIException extends GarvelCheckedException {
    public CLIException(final String errorMessage) {
        super(errorMessage);
    }
}
