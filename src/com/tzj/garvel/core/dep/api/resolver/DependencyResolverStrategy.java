package com.tzj.garvel.core.dep.api.resolver;

import com.tzj.garvel.core.dep.api.Artifact;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.exception.DependencyResolverException;

import java.util.List;

public interface DependencyResolverStrategy {
    List<Artifact> resolve(final DependencyResolverOperation operation) throws DependencyResolverException;
}
