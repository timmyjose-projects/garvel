package com.tzj.garvel.core.dep.resolver;

import com.tzj.garvel.core.dep.api.Artifact;
import com.tzj.garvel.core.dep.api.resolver.DependencyResolverOperation;
import com.tzj.garvel.core.dep.api.resolver.DependencyResolverStrategy;

import java.util.List;

public class AdvancedDependencyResolverStrategy implements DependencyResolverStrategy {
    @Override
    public List<Artifact> resolve(final DependencyResolverOperation operation) {
        return null;
    }
}
