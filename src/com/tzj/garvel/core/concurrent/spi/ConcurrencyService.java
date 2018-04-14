package com.tzj.garvel.core.concurrent.spi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public interface ConcurrencyService<V> {
    ExecutorService getExecutor();

    Future<V> submitJob(Job<V> job);

    void shutdown();
}