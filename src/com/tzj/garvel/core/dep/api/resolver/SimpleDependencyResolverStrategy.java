package com.tzj.garvel.core.dep.api.resolver;

import java.util.List;

/**
 * The basic resolver for dependencies. This resolver does not handle cyclic
 * dependencies. In addition, for version conflicts, it will return the first
 * suitable entry that it finds, leaving any potential errors to the user to handle.
 */
public class SimpleDependencyResolverStrategy implements DependencyResolverStrategy {
    @Override
    public List<String> resolve() {
        return null;
    }
}
