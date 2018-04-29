package com.tzj.garvel.core.dep.graph;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is used to generate monotonically increasing ids for
 * use as Graph vertex ids. The `start` constructor is for
 * the case where an existing Graph needs to be updated.
 * Only one instance of this class should run at any point in time;
 */
public class GraphIdGenerator {
    private AtomicInteger id;

    public GraphIdGenerator() {
        this.id = new AtomicInteger(-1);
    }

    public GraphIdGenerator(final int start) {
        this.id = new AtomicInteger(start);
    }

    public int getId() {
        return id.incrementAndGet();
    }
}
