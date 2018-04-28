package com.tzj.garvel.common.spi.core.command;

import com.tzj.garvel.common.spi.exception.GarvelCheckedException;

public class CommandException extends GarvelCheckedException {
    private static final long serialVersionUID = -5668756067485047278L;
    private String errorMessage;

    public CommandException(final String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorString() {
        return errorMessage;
    }
}
