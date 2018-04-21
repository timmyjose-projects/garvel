package com.tzj.garvel.core.cache.api;

public class HomepageEntry extends CacheEntry {
    private String url;

    public HomepageEntry(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "HomepageEntry{" +
                "url='" + url + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }
}
