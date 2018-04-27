package com.tzj.garvel.core.dep;

import com.tzj.garvel.core.dep.api.DependencyManagerService;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.resolver.DependencyResolverContext;

import java.util.List;

/**
 * Thie module handles all the Dependency Management for Garvel.
 */
public enum DependencyManagerServiceImpl implements DependencyManagerService {
    INSTANCE;

    /**
     * Check the dependencies stored in the Core Cache, resolve the dependencies,
     * and return the classpath string for further processing.
     *
     * @param ctx
     * @return
     * @throws DependencyManagerException
     */
    @Override
    public List<String> analyse(final DependencyResolverContext ctx) throws DependencyManagerException {
        return null;
    }
}
