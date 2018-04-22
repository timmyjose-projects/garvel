package com.tzj.garvel.core.cache.api;

public class MainClassEntry extends CacheEntry {
    private String mainClassPath;

    public MainClassEntry(final String mainClassPath) {
        this.mainClassPath = mainClassPath;
    }

    @Override
    public String toString() {
        return "MainClassEntry{" +
                "mainClassPath='" + mainClassPath + '\'' +
                '}';
    }

    public String getMainClassPath() {
        return mainClassPath;
    }
}
