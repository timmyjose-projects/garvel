package com.tzj.garvel.core.filesystem.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class FilesystemFrameworkException extends GarvelCheckedException {
    private static final long serialVersionUID = 42242782075139131L;

    public FilesystemFrameworkException(final String errorMessage) {
        super(errorMessage);
    }
}
