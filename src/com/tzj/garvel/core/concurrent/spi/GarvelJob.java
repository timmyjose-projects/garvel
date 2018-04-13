package com.tzj.garvel.core.concurrent.spi;

import java.util.concurrent.Callable;

public interface GarvelJob<V> extends Callable<V> {
}
