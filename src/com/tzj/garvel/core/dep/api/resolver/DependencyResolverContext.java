package com.tzj.garvel.core.dep.api.resolver;

public class DependencyResolverContext {
    private DependencyResolverStrategy resolver;

    public DependencyResolverContext(final DependencyResolverStrategy resolver) {
        this.resolver = resolver;
    }

    public void setResolver(final DependencyResolverStrategy resolver) {
        this.resolver = resolver;
    }

    public void resolveStrategy() {
        resolver.resolve();
    }
}
