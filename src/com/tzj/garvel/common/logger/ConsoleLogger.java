package com.tzj.garvel.common.logger;

import com.tzj.garvel.common.spi.logger.Logger;

public class ConsoleLogger implements Logger {
    @Override
    public void log() {
        System.out.println("Hello from console logger");
    }
}
