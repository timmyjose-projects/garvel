package com.tzj.garvel.common.logger;

import com.tzj.garvel.common.spi.logger.Logger;
import com.tzj.garvel.common.spi.logger.LoggerType;

public class LoggerFactory {
    private LoggerFactory() {
    }

    public static Logger getLogger(final LoggerType type) {
        Logger logger = null;

        switch (type) {
            case CONSOLE:
                logger = new ConsoleLogger();
                break;
            case FILE:
                logger = new FileLogger();
                break;
        }

        return logger;
    }
}
