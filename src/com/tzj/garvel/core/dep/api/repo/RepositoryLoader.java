package com.tzj.garvel.core.dep.api.repo;

import com.tzj.garvel.core.dep.api.exception.RepositoryLoaderException;

/**
 * The common functionality associated with a repository goes
 * here.
 */
public abstract class RepositoryLoader {
    protected RepositoryLoader nextLoader;
    protected RepositoryKind kind;

    public RepositoryLoader() {
    }

    protected abstract void setNextLoader();
    protected abstract boolean checkRepoStatus();
    public abstract String constructArtifactUrl(final String groupId, final String artifactId) throws RepositoryLoaderException;
}
