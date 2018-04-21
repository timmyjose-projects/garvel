package com.tzj.garvel.core.cache.api;

import java.util.HashMap;
import java.util.Map;

public class BinSectionEntry extends CacheEntry {
    private Map<String, String> targets;

    public BinSectionEntry() {
        this.targets = new HashMap<>();
    }

    @Override
    public String toString() {
        return "BinSectionEntry{" +
                "targets=" + targets +
                '}';
    }

    public Map<String, String> getTargets() {
        return targets;
    }
}
