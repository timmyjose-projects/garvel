package com.tzj.garvel.core.dep.api;

import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.resolver.DependencyResolverContext;

import java.util.List;

public interface DependencyManagerService {
    List<String> analyse(final DependencyResolverContext ctx) throws DependencyManagerException;
}
