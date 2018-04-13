package com.tzj.garvel.common.spi.error;

public class GarvelCheckedException extends Exception {
    public GarvelCheckedException(final String errorMessage) {
        super(errorMessage);
    }
}
