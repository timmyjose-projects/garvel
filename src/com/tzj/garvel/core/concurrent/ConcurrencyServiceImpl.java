package com.tzj.garvel.core.concurrent;

import com.tzj.garvel.core.concurrent.api.ConcurrencyService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple wrapper over an ExecutorService.
 *
 */
public enum ConcurrencyServiceImpl implements ConcurrencyService {
    INSTANCE;

    private ExecutorService service;

    @Override
    public ExecutorService getExecutor() {
        // different strategies can be adopted based
        // on project parameters later on
        if (this.service == null) {
            service = Executors.newCachedThreadPool();
        }
        return service;
    }
}
