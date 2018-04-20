package com.tzj.garvel.core.cache.api;

public class CacheName extends CacheEntry {
    private String name;

    public CacheName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
