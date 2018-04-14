package com.tzj.garvel.core.concurrent;

import com.tzj.garvel.core.concurrent.api.ConcurrencyService;
import com.tzj.garvel.core.concurrent.api.Job;

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
    public Future submitJob(final Job job) {
        return service.submit(job);
    }

    @Override
    public void shutdown() {
        if (!service.isShutdown()) {
            service.shutdownNow();
        }
    }
}
