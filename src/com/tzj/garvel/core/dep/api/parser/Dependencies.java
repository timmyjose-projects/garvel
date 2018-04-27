package com.tzj.garvel.core.dep.api.parser;

import com.tzj.garvel.core.dep.api.Artifact;

import java.util.ArrayList;
import java.util.List;

public class Dependencies {
    private List<Artifact> dependencies;

    public Dependencies() {
        this.dependencies = new ArrayList<>();
    }

    public List<Artifact> getDependencies() {
        return dependencies;
    }

    public void addDependency(final Artifact dependency) {
        dependencies.add(dependency);
    }
}
