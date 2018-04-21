package com.tzj.garvel.core.cache.api;

public class LicenceFileEntry extends CacheEntry {
    private String licenceUrl;

    public LicenceFileEntry(final String licenceUrl) {
        this.licenceUrl = licenceUrl;
    }

    @Override
    public String toString() {
        return "LicenceFileEntry{" +
                "licenceUrl='" + licenceUrl + '\'' +
                '}';
    }

    public String getLicenceUrl() {
        return licenceUrl;
    }
}
