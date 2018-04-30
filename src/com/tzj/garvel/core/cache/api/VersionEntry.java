package com.tzj.garvel.core.cache.api;

public class VersionEntry extends CacheEntry {
    private String version;

    public VersionEntry(final String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "VersionEntry{" +
                "version='" + version + '\'' +
                '}';
    }
}
