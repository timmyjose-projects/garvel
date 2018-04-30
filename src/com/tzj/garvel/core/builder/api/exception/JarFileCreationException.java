package com.tzj.garvel.core.builder.api.exception;

import com.tzj.garvel.common.spi.exception.GarvelCheckedException;

public class JarFileCreationException extends BuildException {
    private static final long serialVersionUID = 8905140743010981103L;

    private String errorMessage;

    public JarFileCreationException(final String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorString() {
        return errorMessage;
    }
}
