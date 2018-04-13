package com.tzj.garvel.common.logger;

import com.tzj.garvel.common.spi.logger.Logger;
import com.tzj.garvel.common.spi.logger.LoggerService;
import com.tzj.garvel.common.spi.logger.LoggerType;

public class LoggerServiceImpl implements LoggerService {
    @Override
    public Logger getLogger(final LoggerType type) {
        return LoggerFactory.getLogger(type);
    }
}
