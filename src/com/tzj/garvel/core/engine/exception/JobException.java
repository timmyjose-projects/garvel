package com.tzj.garvel.core.engine.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class JobException extends GarvelCheckedException {
    private static final long serialVersionUID = 2060626337745383210L;

    public JobException(final String message) {
        super(message);
    }
}
