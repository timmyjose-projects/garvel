package com.tzj.garvel.core.dep.api.exception;

import com.tzj.garvel.common.spi.exception.GarvelCheckedException;

public class GraphCheckedException extends GarvelCheckedException {
    private static final long serialVersionUID = 2154984201749335628L;

    public GraphCheckedException(final String errorMessage) {
        super(errorMessage);
    }
}
