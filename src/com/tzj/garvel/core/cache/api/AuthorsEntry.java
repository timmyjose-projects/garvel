package com.tzj.garvel.core.cache.api;

import java.util.List;

public class AuthorsEntry extends CacheEntry {
    private List<String> authors;

    public AuthorsEntry(final List<String> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "AuthorsEntry{" +
                "authors=" + authors +
                '}';
    }

    public List<String> getAuthors() {
        return authors;
    }
}
