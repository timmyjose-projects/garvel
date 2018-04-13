package com.tzj.garvel.core;

import com.tzj.garvel.core.concurrent.spi.ConcurrentService;
import com.tzj.garvel.core.parser.spi.ParserService;

import java.util.ServiceLoader;

public enum CoreModuleLoader {
    INSTANCE;

    public ConcurrentService getConcurrencyFramework() {
        ServiceLoader<ConcurrentService> concurrentServiceLoader = ServiceLoader.load(ConcurrentService.class);

        ConcurrentService concurrentService = null;
        for (ConcurrentService service : concurrentServiceLoader) {
            if (service != null) {
                concurrentService = service;
                break;
            }
        }

        if (concurrentService == null) {
            throw new IllegalStateException("Unable to get ConcurrentService handler");
        }

        return concurrentService;
    }

    public ParserService getParser() {
        ServiceLoader<ParserService> parserServiceLoader = ServiceLoader.load(ParserService.class);

        ParserService parserService = null;
        for (ParserService service : parserServiceLoader) {
            if (service != null) {
                parserService = service;
                break;
            }
        }

        if (parserService == null) {
            throw new IllegalStateException("Unable to get Parsing Service");
        }

        return parserService;
    }
}
