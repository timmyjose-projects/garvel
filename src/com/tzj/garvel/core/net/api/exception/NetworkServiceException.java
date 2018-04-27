package com.tzj.garvel.core.net.api.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class NetworkServiceException extends GarvelCheckedException {
    private static final long serialVersionUID = 959111880726834053L;

    public NetworkServiceException(final String errorMessage) {
        super(errorMessage);
    }
}
