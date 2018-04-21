package com.tzj.garvel.core.cache.api;

public class LicenceEntry extends CacheEntry {
    private String licence;

    public LicenceEntry(final String licence) {
        this.licence = licence;
    }

    @Override
    public String toString() {
        return "LicenceEntry{" +
                "licence='" + licence + '\'' +
                '}';
    }

    public String getLicence() {
        return licence;
    }
}
