package com.tzj.garvel.core.builder.api.exception;

import com.tzj.garvel.common.spi.exception.GarvelCheckedException;

public class BuildException extends GarvelCheckedException {
    private static final long serialVersionUID = 3659427732871862596L;

    public BuildException(final String errorMessage) {
        super(errorMessage);
    }
}
