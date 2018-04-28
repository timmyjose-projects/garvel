package com.tzj.garvel.core.dep.api.resolver;

import com.tzj.garvel.core.dep.api.Artifact;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.exception.DependencyResolverException;

import java.util.List;

/**
 * Context object that manages the current algorithm being used for
 * Dependency Management.
 */
public class DependencyResolverContext {
    private DependencyResolverStrategy resolver;

    public DependencyResolverContext(final DependencyResolverStrategy resolver) {
        this.resolver = resolver;
    }

    public void setResolver(final DependencyResolverStrategy resolver) {
        this.resolver = resolver;
    }

    public List<Artifact> resolveStrategy(final DependencyResolverOperation operation) throws DependencyResolverException {
        return resolver.resolve(operation);
    }
}
