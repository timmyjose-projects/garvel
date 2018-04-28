package com.tzj.garvel.core.net.api.exception.buffers;

import com.tzj.garvel.common.spi.error.GarvelUncheckedException;

public class BufferException extends GarvelUncheckedException {
    private static final long serialVersionUID = -4937919903710603300L;

    public BufferException() {
        super();
    }

    public BufferException(final String errorMessage) {
        super(errorMessage);
    }
}
