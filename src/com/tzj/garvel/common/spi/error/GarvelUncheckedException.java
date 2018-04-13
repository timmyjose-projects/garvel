package com.tzj.garvel.common.spi.error;

public class GarvelUncheckedException extends RuntimeException {
    public GarvelUncheckedException() {
    }

    public GarvelUncheckedException(final String errorMessage) {
        super(errorMessage);
    }
}
