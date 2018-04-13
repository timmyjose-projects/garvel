package com.tzj.garvel.core.filesystem.exception;

import com.tzj.garvel.common.spi.error.GarvelCheckedException;

public class FilesystemFrameworkException extends GarvelCheckedException {
    public FilesystemFrameworkException(final String errorMessage) {
        super(errorMessage);
    }
}
