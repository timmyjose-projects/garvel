package com.tzj.garvel.common.spi.core;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.CoreServiceImpl;

/**
 * Mediate access to the core services for clients.
 */
public enum CoreServiceLoader {
    INSTANCE;

    public CoreService getCoreService() {
        return CoreServiceImpl.INSTANCE;
    }
}
