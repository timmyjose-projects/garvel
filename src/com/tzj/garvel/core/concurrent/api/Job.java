package com.tzj.garvel.core.concurrent.api;

import com.tzj.garvel.common.spi.core.command.CommandResult;

import java.util.concurrent.Callable;

public interface Job<V> extends Callable<V> {
}
