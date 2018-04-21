package com.tzj.garvel.core.cache.api;

public class ReadmeEntry extends CacheEntry {
    private String url;

    public ReadmeEntry(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ReadmeEntry{" +
                "url='" + url + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }
}
