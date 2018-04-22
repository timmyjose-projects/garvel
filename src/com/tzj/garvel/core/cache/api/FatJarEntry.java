package com.tzj.garvel.core.cache.api;

public class FatJarEntry extends CacheEntry {
    private boolean isFatJar;

    public FatJarEntry(final boolean isFatJar) {
        this.isFatJar = isFatJar;
    }

    @Override
    public String toString() {
        return "FatJarEntry{" +
                "isFatJar=" + isFatJar +
                '}';
    }

    public boolean isFatJar() {
        return isFatJar;
    }
}
