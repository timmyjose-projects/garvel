package com.tzj.garvel.core.cache.api;

import java.util.List;

public class KeywordsEntry extends CacheEntry {
    private List<String> keywords;

    public KeywordsEntry(final List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "KeywordsEntry{" +
                "keywords=" + keywords +
                '}';
    }

    public List<String> getKeywords() {
        return keywords;
    }
}
