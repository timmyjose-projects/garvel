package com.tzj.garvel.core.dep.api.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class RepositoryLoaderException extends GarvelCheckedException {
    private static final long serialVersionUID = -2541567567885554380L;

    public RepositoryLoaderException(final String errorMessage) {
        super(errorMessage);
    }
}
