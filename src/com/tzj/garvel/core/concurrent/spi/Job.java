package com.tzj.garvel.core.concurrent.spi;

import java.util.concurrent.Callable;

public interface Job<V> extends Callable<V> {
}
