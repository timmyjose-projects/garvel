package com.tzj.garvel.core.cache.api;

import java.util.List;

public class CategoriesEntry extends CacheEntry {
    private List<String> categories;

    public CategoriesEntry(final List<String> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "CategoriesEntry{" +
                "categories=" + categories +
                '}';
    }

    public List<String> getCategories() {
        return categories;
    }
}
