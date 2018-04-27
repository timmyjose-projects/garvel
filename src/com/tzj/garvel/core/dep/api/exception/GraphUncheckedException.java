package com.tzj.garvel.core.dep.api.exception;

import com.tzj.garvel.common.spi.error.GarvelUncheckedException;

public class GraphUncheckedException extends GarvelUncheckedException {
    private static final long serialVersionUID = 6483349032725046609L;

    public GraphUncheckedException(final String errorMessage) {
        super(errorMessage);
    }
}
