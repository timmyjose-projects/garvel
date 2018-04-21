package com.tzj.garvel.core.cache.api;

import java.util.List;

public class ClassPathEntry extends CacheEntry {
    private List<String> paths;

    public ClassPathEntry(final List<String> paths) {
        this.paths = paths;
    }

    @Override
    public String toString() {
        return "ClassPathEntry{" +
                "paths=" + paths +
                '}';
    }

    public List<String> getPaths() {
        return paths;
    }
}
