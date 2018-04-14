package com.tzj.garvel.core.concurrent.api;

import java.util.concurrent.Callable;

public interface Job<V> extends Callable<V> {
}
