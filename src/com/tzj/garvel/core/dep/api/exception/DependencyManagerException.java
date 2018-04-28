package com.tzj.garvel.core.dep.api.exception;

import com.tzj.garvel.common.spi.exception.GarvelCheckedException;

public class DependencyManagerException extends GarvelCheckedException {
    private static final long serialVersionUID = 7504323886334647158L;

    public DependencyManagerException(final String errorMessage) {
        super(errorMessage);
    }
}
