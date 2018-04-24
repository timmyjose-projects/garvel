package com.tzj.garvel.common.spi.error;

public class GarvelCheckedException extends Exception {
    private static final long serialVersionUID = -2538293604499766859L;

    private String errorMessage;

    public GarvelCheckedException(final String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public GarvelCheckedException() {
        super();
    }

    public String getErrorString() {
        return errorMessage;
    }
}
