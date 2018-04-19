package com.tzj.garvel.core.concurrent.api;

import com.tzj.garvel.common.spi.core.command.CommandResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public interface ConcurrencyService {
    ExecutorService getExecutor();
}
