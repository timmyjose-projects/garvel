package com.tzj.garvel.core.cache.api;

public class DescriptionEntry extends CacheEntry {
    private String description;

    public DescriptionEntry(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DescriptionEntry{" +
                "description='" + description + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }
}
