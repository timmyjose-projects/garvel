package com.tzj.garvel.cli.exception;

import com.tzj.garvel.common.spi.exception.GarvelCheckedException;

public class CLIException extends GarvelCheckedException {
    private static final long serialVersionUID = 9032281829152703107L;

    public CLIException(final String errorMessage) {
        super(errorMessage);
    }
}
