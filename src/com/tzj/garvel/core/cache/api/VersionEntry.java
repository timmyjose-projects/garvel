package com.tzj.garvel.core.cache.api;

public class VersionEntry extends CacheEntry {
    private String name;

    public VersionEntry(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "VersionEntry{" +
                "name='" + name + '\'' +
                '}';
    }
}
