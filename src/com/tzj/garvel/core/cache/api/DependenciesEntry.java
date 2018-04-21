package com.tzj.garvel.core.cache.api;

import com.tzj.garvel.core.parser.api.visitor.semver.SemverKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependenciesEntry extends CacheEntry {
    private Map<String, Map<SemverKey, List<String>>> dependencies;

    public DependenciesEntry() {
        this.dependencies = new HashMap<>();
    }

    public Map<String, Map<SemverKey, List<String>>> getDependencies() {
        return dependencies;
    }

    @Override
    public String toString() {
        return "DependenciesEntry{" +
                "dependencies=" + dependencies +
                '}';
    }
}
