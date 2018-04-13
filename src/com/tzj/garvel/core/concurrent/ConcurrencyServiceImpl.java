package com.tzj.garvel.core.concurrent;

import com.tzj.garvel.core.concurrent.spi.ConcurrencyService;
import com.tzj.garvel.core.concurrent.spi.GarvelJob;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    @Override
    public Future submitTask(final GarvelJob job) {
        return service.submit(job);
    }

    @Override
    public void shutdown() {
        if (!service.isShutdown()) {
            service.shutdownNow();
        }
    }
}
