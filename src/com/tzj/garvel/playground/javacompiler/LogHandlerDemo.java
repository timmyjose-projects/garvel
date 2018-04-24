package com.tzj.garvel.playground.javacompiler;

import com.tzj.garvel.core.GarvelCoreConstants;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogHandlerDemo {
    public static void main(String[] args) throws IOException {
        final String logConfigFile = GarvelCoreConstants.GARVEL_PROJECT_ROOT + "/config/logger.config";

        System.setProperty("java.util.logging.config.file", logConfigFile);
        LogManager manager = LogManager.getLogManager();
        manager.readConfiguration();

        Logger testLogger = Logger.getLogger(LogHandlerDemo.class.getName());

//        while (true) {
            testLogger.log(Level.FINEST, "Finest test");
            testLogger.log(Level.FINER, "Finer test");
            testLogger.log(Level.FINE, "Fine test");
            testLogger.log(Level.WARNING, "Warning test");
            testLogger.log(Level.INFO, "Info test");
            testLogger.log(Level.SEVERE, "Severe test");
        }
  //  }
}
