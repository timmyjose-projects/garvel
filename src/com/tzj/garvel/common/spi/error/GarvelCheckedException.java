package com.tzj.garvel.common.spi.error;

public class GarvelCheckedException extends Exception {
    private static final long serialVersionUID = -2538293604499766859L;

    public GarvelCheckedException(final String errorMessage) {
        super(errorMessage);
    }

    public GarvelCheckedException() {
        super();
    }
}
