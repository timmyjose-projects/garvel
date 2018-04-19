package com.tzj.garvel.common.spi.error;

public class GarvelUncheckedException extends RuntimeException {
    private static final long serialVersionUID = 4753313678382007239L;

    public GarvelUncheckedException() {
    }

    public GarvelUncheckedException(final String errorMessage) {
        super(errorMessage);
    }
}
