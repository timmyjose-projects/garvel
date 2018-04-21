package com.tzj.garvel.core.cache.api;

public class NameEntry extends CacheEntry {
    private String name;

    public NameEntry(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "NameEntry{" +
                "name='" + name + '\'' +
                '}';
    }
}
