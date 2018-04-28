package com.tzj.garvel.core.cache.exception;

import com.tzj.garvel.common.spi.exception.GarvelCheckedException;

public class CacheManagerException extends GarvelCheckedException {
    private static final long serialVersionUID = -5772762986281423129L;

    public CacheManagerException(final String errorMessage) {
        super(errorMessage);
    }
}
